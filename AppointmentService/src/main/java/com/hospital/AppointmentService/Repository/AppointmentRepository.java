package com.hospital.AppointmentService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.AppointmentService.Entities.Appointment;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  List<Appointment> findByPatientId(Long patientId);
  List<Appointment> findByDoctorIdAndStartTimeBetween(Long doctorId,LocalDateTime start, LocalDateTime end);
}

