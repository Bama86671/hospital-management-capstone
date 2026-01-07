package com.hospital.AppointmentService.Dto;


import com.hospital.AppointmentService.Entities.Appointment;

import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
	@NotNull 
	private Appointment.Status status;

	public Appointment.Status getStatus() {
		return status;
	}

	public void setStatus(Appointment.Status status) {
		this.status = status;
	}
	

}
