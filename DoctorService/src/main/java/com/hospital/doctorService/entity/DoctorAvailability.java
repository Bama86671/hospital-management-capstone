package com.hospital.doctorService.entity;

import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
@Entity
public class DoctorAvailability {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "doctor_id")
	    private Doctor doctor;

	    @Enumerated(EnumType.STRING)
	    private DayOfWeek dayOfWeek;

	    private LocalTime startTime;
	    private LocalTime endTime;

	    private boolean isAvailable; // For marking availability

		public DoctorAvailability(Long id, Doctor doctor, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime,
				boolean isAvailable) {
			super();
			this.id = id;
			this.doctor = doctor;
			this.dayOfWeek = dayOfWeek;
			this.startTime = startTime;
			this.endTime = endTime;
			this.isAvailable = isAvailable;
		}

		public DoctorAvailability() {
			// TODO Auto-generated constructor stub
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Doctor getDoctor() {
			return doctor;
		}

		public void setDoctor(Doctor doctor) {
			this.doctor = doctor;
		}

		public DayOfWeek getDayOfWeek() {
			return dayOfWeek;
		}

		public void setDayOfWeek(DayOfWeek dayOfWeek) {
			this.dayOfWeek = dayOfWeek;
		}

		public LocalTime getStartTime() {
			return startTime;
		}

		public void setStartTime(LocalTime startTime) {
			this.startTime = startTime;
		}

		public LocalTime getEndTime() {
			return endTime;
		}

		public void setEndTime(LocalTime endTime) {
			this.endTime = endTime;
		}

		public boolean isAvailable() {
			return isAvailable;
		}

		public void setAvailable(boolean isAvailable) {
			this.isAvailable = isAvailable;
		}

	

}
