package com.hospital.AppointmentService.Controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hospital.AppointmentService.Dto.AddToCartRequest;
import com.hospital.AppointmentService.Dto.AvailabilityResponse;
import com.hospital.AppointmentService.Dto.CreateAppointmentRequest;
import com.hospital.AppointmentService.Dto.UpdateStatusRequest;
import com.hospital.AppointmentService.Entities.Appointment;
import com.hospital.AppointmentService.Entities.CartItem;
import com.hospital.AppointmentService.Service.AppointmentService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

  private final AppointmentService service;

  public AppointmentController(AppointmentService service) { this.service = service; }

  // Add to cart
  @PostMapping("/cart")
  public ResponseEntity<CartItem> addToCart(@Valid @RequestBody AddToCartRequest req) {
    CartItem item = service.addToCart(req);
    return ResponseEntity.created(URI.create("/appointments/cart/" + item.getId())).body(item);
  }

  // View cart for patient
  @GetMapping("/cart/{patientId}")
  public List<CartItem> viewCart(@PathVariable Long patientId) {
    return service.viewCart(patientId);
  }

  // Checkout cart item -> Appointment
  @PostMapping("/cart/{cartId}/checkout")
  public ResponseEntity<Appointment> checkout(@PathVariable Long cartId) {
    Appointment appt = service.checkout(cartId);
    return ResponseEntity.created(URI.create("/appointments/" + appt.getId())).body(appt);
  }

  // Book direct by specialization
  @PostMapping
  public ResponseEntity<Appointment> book(@Valid @RequestBody CreateAppointmentRequest req) {
    Appointment appt = service.book(req);
    return ResponseEntity.created(URI.create("/appointments/" + appt.getId())).body(appt);
  }

  // View appointment
  @GetMapping("/{id}")
  public Appointment get(@PathVariable Long id) {
    return service.getById(id);
  }

  // List for patient
  @GetMapping("/patient/{patientId}")
  public List<Appointment> listForPatient(@PathVariable Long patientId) {
    return service.listForPatient(patientId);
  }

  // Update status: CONFIRMED/CANCELLED/COMPLETED
  @PatchMapping("/{id}/status")
  public Appointment updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest req) {
    return service.updateStatus(id, req.getStatus());
  }
  @PostMapping("/availability") 
  public AvailabilityResponse checkAvailability(@RequestParam Long doctorId, @RequestParam LocalDateTime from, @RequestParam LocalDateTime to) { 
	  return service.checkAvailability(doctorId, from, to); }
}
