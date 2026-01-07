package com.hospital.AppointmentService.Service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hospital.AppointmentService.Dto.AddToCartRequest;
import com.hospital.AppointmentService.Dto.AvailabilityResponse;
import com.hospital.AppointmentService.Dto.CreateAppointmentRequest;
import com.hospital.AppointmentService.Entities.Appointment;
import com.hospital.AppointmentService.Entities.CartItem;
import com.hospital.AppointmentService.Exception.ConflictException;
import com.hospital.AppointmentService.Repository.AppointmentRepository;
import com.hospital.AppointmentService.Repository.CartItemRepository;
import com.hospital.AppointmentService.openFeign.DoctorClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AppointmentService {

  private final AppointmentRepository apptRepo;
  private final CartItemRepository cartRepo;
  private final DoctorClient doctorClient;

  public AppointmentService(AppointmentRepository apptRepo,
                            CartItemRepository cartRepo,
                            DoctorClient doctorClient) {
    this.apptRepo = apptRepo;
    this.cartRepo = cartRepo;
    this.doctorClient = doctorClient;
  }

  // Validate times
  private void validateTime(LocalDateTime start, LocalDateTime end) {
    if (!end.isAfter(start)) throw new BadRequestException("endTime must be after startTime");
    if (start.isBefore(LocalDateTime.now())) throw new BadRequestException("startTime must be in the future");
  }

  // Add to cart with circuit breaker on doctor-service
  @Transactional
  @CircuitBreaker(name = "DoctorService", fallbackMethod = "addToCartFallback")
  @Retry(name = "DoctorService")
  public CartItem addToCart(AddToCartRequest req) {
    validateTime(req.getStartTime(), req.getEndTime());

    List<DoctorClient.DoctorDto> doctors = doctorClient.getDoctorsBySpecialization(req.getSpecialization());
    DoctorClient.DoctorDto doctor = doctors.stream()
      .filter(d -> d.available)
      .min(Comparator.comparing(d -> d.id)) // simple selection policy
      .orElseThrow(() -> new ConflictException("No available doctors for specialization"));

    CartItem item = new CartItem();
    item.setPatientId(req.getPatientId());
    item.setDoctorId(doctor.id);
    item.setDoctorSpecialization(doctor.specialization);
    item.setStartTime(req.getStartTime());
    item.setEndTime(req.getEndTime());
    return cartRepo.save(item);
  }

  // Fallback: add to cart with specialization only
  protected CartItem addToCartFallback(AddToCartRequest req, Throwable ex) {
    validateTime(req.getStartTime(), req.getEndTime());
    CartItem item = new CartItem();
    item.setPatientId(req.getPatientId());
    item.setDoctorId(0L); // unknown placeholder
    item.setDoctorSpecialization(req.getSpecialization());
    item.setStartTime(req.getStartTime());
    item.setEndTime(req.getEndTime());
    return cartRepo.save(item);
  }

  public List<CartItem> viewCart(Long patientId) {
    return cartRepo.findByPatientId(patientId);
  }

  // Book appointment directly (specialization-driven), with availability check and circuit breaker
  @Transactional
  @CircuitBreaker(name = "DoctorService", fallbackMethod = "bookFallbackPending")
  @Retry(name = "DoctorService")
  public Appointment book(CreateAppointmentRequest req) {
    validateTime(req.getStartTime(), req.getEndTime());

    List<DoctorClient.DoctorDto> doctors = doctorClient.getDoctorsBySpecialization(req.getSpecialization());
    DoctorClient.DoctorDto doctor = doctors.stream()
      .filter(d -> d.available)
      .findFirst()
      .orElseThrow(() -> new ConflictException("No available doctors"));

    // optional: check availability via doctor-service (if exposed)
    DoctorClient.AvailabilityDto avail = doctorClient.checkAvailability(
      doctor.id, req.getStartTime().toString(), req.getEndTime().toString());
    if (!avail.available) throw new ConflictException("Doctor not available: " + avail.message);

    if (!apptRepo.findByDoctorIdAndStartTimeBetween(doctor.id, req.getStartTime(), req.getEndTime()).isEmpty()) {
      throw new ConflictException("Slot already booked");
    }

    Appointment appt = new Appointment();
    appt.setPatientId(req.getPatientId());
    appt.setDoctorId(doctor.id);
    appt.setDoctorSpecialization(doctor.specialization);
    appt.setStartTime(req.getStartTime());
    appt.setEndTime(req.getEndTime());
    appt.setStatus(Appointment.Status.CONFIRMED);
    return apptRepo.save(appt);
  }

  // Fallback: create PENDING appointment when doctor-service is down
  protected Appointment bookFallbackPending(CreateAppointmentRequest req, Throwable ex) {
    validateTime(req.getStartTime(), req.getEndTime());
    Appointment appt = new Appointment();
    appt.setPatientId(req.getPatientId());
    appt.setDoctorId(0L);
    appt.setDoctorSpecialization(req.getSpecialization());
    appt.setStartTime(req.getStartTime());
    appt.setEndTime(req.getEndTime());
    appt.setStatus(Appointment.Status.PENDING);
    return apptRepo.save(appt);
  }

  // Checkout cart -> Appointment (CONFIRMED or PENDING via fallback)
  @Transactional
  @CircuitBreaker(name = "DoctorService", fallbackMethod = "checkoutFallbackPending")
  @Retry(name = "DoctorService")
  public Appointment checkout(Long cartId){
    CartItem item = cartRepo.findById(cartId).orElseThrow(() -> new NotFoundException("Cart item not found"));
    if (item.isLocked()) throw new ConflictException("Cart item already used");

    if (!apptRepo.findByDoctorIdAndStartTimeBetween(item.getDoctorId(), item.getStartTime(), item.getEndTime()).isEmpty()) {
      throw new ConflictException("Slot already booked");
    }

    Appointment appt = new Appointment();
    appt.setPatientId(item.getPatientId());
    appt.setDoctorId(item.getDoctorId());
    appt.setDoctorSpecialization(item.getDoctorSpecialization());
    appt.setStartTime(item.getStartTime());
    appt.setEndTime(item.getEndTime());
    appt.setStatus(Appointment.Status.CONFIRMED);

    item.setLocked(true);
    cartRepo.save(item);
    return apptRepo.save(appt);
  }

  protected Appointment checkoutFallbackPending(Long cartId, Throwable ex) {
    CartItem item = cartRepo.findById(cartId).orElseThrow(() -> new NotFoundException("Cart item not found"));
    Appointment appt = new Appointment();
    appt.setPatientId(item.getPatientId());
    appt.setDoctorId(item.getDoctorId());
    appt.setDoctorSpecialization(item.getDoctorSpecialization());
    appt.setStartTime(item.getStartTime());
    appt.setEndTime(item.getEndTime());
    appt.setStatus(Appointment.Status.PENDING);
    item.setLocked(true);
    cartRepo.save(item);
    return apptRepo.save(appt);
  }

  public Appointment getById(Long id) {
    return apptRepo.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
  }

  public List<Appointment> listForPatient(Long patientId) {
    return apptRepo.findByPatientId(patientId);
  }

  @Transactional
  public Appointment updateStatus(Long id, Appointment.Status status) {
    Appointment appt = getById(id);
    appt.setStatus(status);
    return apptRepo.save(appt);
  }

public AvailabilityResponse checkAvailability(Long doctorId, LocalDateTime from, LocalDateTime to) {
	return new AvailabilityResponse(true, "Doctor is available");

	
}
}

