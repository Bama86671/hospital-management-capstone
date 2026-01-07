package com.hospital.AppointmentService.Dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.*;

public class AddToCartRequest {
	
	@NotNull private 
	Long patientId; @NotNull 
	private String specialization; 
	@NotNull @Future 
	private LocalDateTime startTime; 
	@NotNull @Future 
	private LocalDateTime endTime;
	public Long getPatientId() {
		return patientId;
	}
	public String getSpecialization() {
		return specialization;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	

}
