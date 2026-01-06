package com.hospital.AppointmentService.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Appointment {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long patientId;
  private Long doctorId;
  private String doctorSpecialization;

  private LocalDateTime startTime;
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  private Status status;

  public enum Status { PENDING, CONFIRMED, CANCELLED, COMPLETED }

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Long getPatientId() {
	return patientId;
}

public void setPatientId(Long patientId) {
	this.patientId = patientId;
}

public Long getDoctorId() {
	return doctorId;
}

public void setDoctorId(Long doctorId) {
	this.doctorId = doctorId;
}

public String getDoctorSpecialization() {
	return doctorSpecialization;
}

public void setDoctorSpecialization(String doctorSpecialization) {
	this.doctorSpecialization = doctorSpecialization;
}

public LocalDateTime getStartTime() {
	return startTime;
}

public void setStartTime(LocalDateTime startTime) {
	this.startTime = startTime;
}

public LocalDateTime getEndTime() {
	return endTime;
}

public void setEndTime(LocalDateTime endTime) {
	this.endTime = endTime;
}

public Status getStatus() {
	return status;
}

public void setStatus(Status status) {
	this.status = status;
}
  
  
}
