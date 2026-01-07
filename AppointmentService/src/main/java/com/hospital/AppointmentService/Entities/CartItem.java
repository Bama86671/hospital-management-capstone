package com.hospital.AppointmentService.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
public class CartItem {
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

  @Column(nullable = false) 
  private boolean locked = false;

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

public boolean isLocked() {
	return locked;
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

public void setLocked(boolean locked) {
	this.locked = locked;
}
  
  }


