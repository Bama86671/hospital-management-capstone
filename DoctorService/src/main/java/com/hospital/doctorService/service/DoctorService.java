package com.hospital.doctorService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hospital.doctorService.entity.Doctor;
import com.hospital.doctorService.entity.DoctorAvailability;
import com.hospital.doctorService.exception.DoctorAlreadyExistsException;
import com.hospital.doctorService.exception.DoctorNotFoundException;
import com.hospital.doctorService.exception.InvalidAvailabilityException;
import com.hospital.doctorService.repository.DoctorRepository;
import com.hospital.doctorService.repository.DoctorAvailabilityRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorAvailabilityRepository doctorAvailabilityRepository;

    // Get doctor by ID
    public Doctor getDoctorById(Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);
        return doctorOptional.orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + doctorId + " not found"));
    }

    // Create doctor
    public Doctor createDoctor(Doctor doctor) {

        if (doctorRepository.existsByEmail(doctor.getEmail())) {
            throw new DoctorAlreadyExistsException("Email already exists");
        }

        return doctorRepository.save(doctor);
    }


    // View doctor (Fix: method was incorrectly implemented)
    public Doctor viewDoctor(Long doctorId) {
        return doctorRepository.findById(doctorId)
            .orElseThrow(() -> new DoctorNotFoundException("Doctor with ID " + doctorId + " not found"));
    }

    // Update doctor profile
    public Doctor updateDoctor(Long doctorId, Doctor doctorDetails) {
        Doctor doctor = getDoctorById(doctorId);
        if (doctor != null) {
        	if(doctorDetails.getName()!=null) {
        		doctor.setName(doctorDetails.getName());
        	}
        	if(doctorDetails.getSpecialization()!=null) {
        		doctor.setSpecialization(doctorDetails.getSpecialization());
        	}
        	if(doctorDetails.getMobileNumber()!=null) {
        		doctor.setMobileNumber(doctorDetails.getMobileNumber());
        	}
        	if(doctorDetails.getQualification()!=null) {
        		doctor.setQualification(doctorDetails.getQualification());
        	}
        	if(doctorDetails.getExperience()!=null) {
        		doctor.setExperience(doctorDetails.getExperience());
        	}
            
            return doctorRepository.save(doctor);
        }
        return null; // Or throw an exception if the doctor is not found
    }

    // Set doctor availability
    public DoctorAvailability setAvailability(Long doctorId,
    		DayOfWeek dayOfWeek, LocalTime startTime, 
    		LocalTime endTime, boolean isAvailable) {
        Doctor doctor = getDoctorById(doctorId);

        // Validate availability times
        if (startTime.isAfter(endTime)) {
            throw new InvalidAvailabilityException("Start "
            		+ "time cannot be after end time.");
        }

        DoctorAvailability availability = new DoctorAvailability();
        availability.setDoctor(doctor);
        availability.setDayOfWeek(dayOfWeek);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setAvailable(isAvailable);
        return doctorAvailabilityRepository.save(availability);
    }

    // Get doctor availability
    public List<DoctorAvailability> getAvailability(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        List<DoctorAvailability> availabilityList =
                doctorAvailabilityRepository.findByDoctor(doctor);
        if (availabilityList.isEmpty()) {
            throw new InvalidAvailabilityException(
                    "Doctor has no availability slots"
            );
        }
        return availabilityList;
    }



    // Update doctor availability
    public DoctorAvailability updateAvailability(Long availabilityId, 
    		LocalTime startTime, LocalTime endTime, boolean isAvailable) {
        DoctorAvailability availability = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new InvalidAvailabilityException("Availability with ID " + availabilityId + " not found"));

        // Validate availability times
        if (startTime.isAfter(endTime)) {
            throw new InvalidAvailabilityException("Start time cannot be after end time.");
        }

        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setAvailable(isAvailable);
        return doctorAvailabilityRepository.save(availability);
    }

    // Mark availability as unavailable
    public DoctorAvailability markUnavailable(Long availabilityId) {
        DoctorAvailability availability = doctorAvailabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new InvalidAvailabilityException("Availability with ID " + availabilityId + " not found"));

        availability.setAvailable(false);
        return doctorAvailabilityRepository.save(availability);
    }
}
