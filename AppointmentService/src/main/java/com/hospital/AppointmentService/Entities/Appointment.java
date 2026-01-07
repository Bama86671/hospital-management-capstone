package com.hospital.AppointmentService.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false) 
  private Long patientId;
  @Column(nullable = false) 
  private Long doctorId;
  @Column(nullable = false) 
  private String doctorSpecialization;

  @Column(nullable = false) 
  private LocalDateTime startTime;
  @Column(nullable = false) 
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING) 
  @Column(nullable = false)
  private Status status = Status.PENDING;

  public enum Status { PENDING, CONFIRMED, CANCELLED, COMPLETED }

public Long getId() {
	return id;
}

public Long getPatientId() {
	return patientId;
}

public Long getDoctorId() {
	return doctorId;
}

public String getDoctorSpecialization() {
	return doctorSpecialization;
}

public LocalDateTime getStartTime() {
	return startTime;
}

public LocalDateTime getEndTime() {
	return endTime;
}

public Status getStatus() {
	return status;
}

public void setId(Long id) {
	this.id = id;
}

public void setPatientId(Long patientId) {
	this.patientId = patientId;
}

public void setDoctorId(Long doctorId) {
	this.doctorId = doctorId;
}

public void setDoctorSpecialization(String doctorSpecialization) {
	this.doctorSpecialization = doctorSpecialization;
}

public void setStartTime(LocalDateTime startTime) {
	this.startTime = startTime;
}

public void setEndTime(LocalDateTime endTime) {
	this.endTime = endTime;
}

public void setStatus(Status status) {
	this.status = status;
}
  
}


