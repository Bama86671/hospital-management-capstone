package com.hospital.AppointmentService.Service;

import org.springframework.stereotype.Service;

import com.hospital.AppointmentService.Entities.Appointment;
import com.hospital.AppointmentService.Repository.AppointmentRepository;
import com.hospital.AppointmentService.openFeign.DoctorClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
  private final AppointmentRepository repo;
  private final DoctorClient doctorClient;

  public AppointmentService(AppointmentRepository repo, DoctorClient doctorClient) {
    this.repo = repo;
    this.doctorClient = doctorClient;
  }

  public Appointment bookBySpecialization(Long patientId, String specialization,
                                          LocalDateTime start, LocalDateTime end) {
    List<DoctorClient.DoctorDto> doctors = doctorClient.getDoctorsBySpecialization(specialization);
    if (doctors.isEmpty()) throw new RuntimeException("No doctors found");

    DoctorClient.DoctorDto doctor = doctors.stream()
                                           .filter(d -> d.available)
                                           .findFirst()
                                           .orElseThrow(() -> new RuntimeException("No available doctors"));

    Appointment appt = new Appointment();
    appt.setPatientId(patientId);
    appt.setDoctorId(doctor.id);
    appt.setDoctorSpecialization(doctor.specialization);
    appt.setStartTime(start);
    appt.setEndTime(end);
    appt.setStatus(Appointment.Status.CONFIRMED);

    return repo.save(appt);
  }

  public List<Appointment> listForPatient(Long patientId) {
    return repo.findByPatientId(patientId);
  }

  public Appointment updateStatus(Long id, Appointment.Status status) {
    Appointment appt = repo.findById(id).orElseThrow();
    appt.setStatus(status);
    return repo.save(appt);
  }
}

