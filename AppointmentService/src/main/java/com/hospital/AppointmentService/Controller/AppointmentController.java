package com.hospital.AppointmentService.Controller;

import org.springframework.web.bind.annotation.*;

import com.hospital.AppointmentService.Entities.Appointment;
import com.hospital.AppointmentService.Service.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  private final AppointmentService service;

  public AppointmentController(AppointmentService service) {
    this.service = service;
  }

  @PostMapping("/book-by-specialization")
  public Appointment book(@RequestParam Long patientId,
                          @RequestParam String specialization,
                          @RequestParam LocalDateTime start,
                          @RequestParam LocalDateTime end) {
    return service.bookBySpecialization(patientId, specialization, start, end);
  }

  @GetMapping("/patient/{patientId}")
  public List<Appointment> list(@PathVariable Long patientId) {
    return service.listForPatient(patientId);
  }

  @PatchMapping("/{id}/status")
  public Appointment updateStatus(@PathVariable Long id, @RequestParam Appointment.Status status) {
    return service.updateStatus(id, status);
  }
}
