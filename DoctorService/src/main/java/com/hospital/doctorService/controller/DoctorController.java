package com.hospital.doctorService.controller;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.doctorService.entity.Doctor;
import com.hospital.doctorService.entity.DoctorAvailability;
import com.hospital.doctorService.exception.InvalidAvailabilityException;
import com.hospital.doctorService.service.DoctorService;

import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    // Create doctor profile
    @PostMapping("/add")
    public ResponseEntity<Doctor> createDoctor(@Valid @RequestBody Doctor doctor) {
        Doctor createdDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
    }

    // View doctor profile
    @GetMapping("/{doctorId}")
    public ResponseEntity<Doctor> viewDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.viewDoctor(doctorId);  // Call service method to fetch doctor by ID
        return ResponseEntity.ok(doctor);
    }

    // Update doctor profile
    @PatchMapping("/update/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @Valid @RequestBody Doctor doctorDetails) {
        Doctor updatedDoctor = doctorService.updateDoctor(doctorId, doctorDetails);
        return updatedDoctor != null 
            ? ResponseEntity.ok(updatedDoctor)
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Set availability
    @PostMapping("/{doctorId}/availability")
    public ResponseEntity<DoctorAvailability> setAvailability(@PathVariable Long doctorId, 
    								@RequestParam DayOfWeek dayOfWeek,
    								@RequestParam LocalTime startTime, 
    								@RequestParam LocalTime endTime,
    								@RequestParam boolean isAvailable) {
    	 DoctorAvailability availability =
    	            doctorService.setAvailability(doctorId, dayOfWeek, startTime, endTime, isAvailable);

    	    return ResponseEntity.status(HttpStatus.CREATED).body(availability);
    }

    // Get availability schedule
    @GetMapping("/{doctorId}/availability")
    public ResponseEntity<List<DoctorAvailability>> getAvailability(@PathVariable Long doctorId) {
            List<DoctorAvailability> availabilityList = doctorService.getAvailability(doctorId);
            return ResponseEntity.ok(availabilityList);
        
    }
 // Controller Layer
    @ExceptionHandler(InvalidAvailabilityException.class)
    public ResponseEntity<String> handleInvalidAvailabilityException(InvalidAvailabilityException ex) {
        // Return the exception message with 404 NOT FOUND or 400 BAD REQUEST as needed
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


    // Update availability
    @PatchMapping("/availability/{availabilityId}")
    public ResponseEntity<DoctorAvailability> updateAvailability(@PathVariable Long availabilityId,
                                                                 @RequestParam LocalTime startTime, 
                                                                 @RequestParam LocalTime endTime,
                                                                 @RequestParam boolean isAvailable) {
        try {
            DoctorAvailability updatedAvailability = doctorService
            		.updateAvailability(availabilityId, startTime, endTime, isAvailable);
            return ResponseEntity.ok(updatedAvailability);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Mark availability as unavailable
    @PatchMapping("/availability/{availabilityId}/unavailable")
    public ResponseEntity<DoctorAvailability> markUnavailable(@PathVariable Long availabilityId) {
        try {
            DoctorAvailability availability = doctorService.markUnavailable(availabilityId);
            return ResponseEntity.ok(availability);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
