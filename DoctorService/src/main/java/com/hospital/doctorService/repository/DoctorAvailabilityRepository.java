package com.hospital.doctorService.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hospital.doctorService.entity.Doctor;
import com.hospital.doctorService.entity.DoctorAvailability;

import java.time.DayOfWeek;
import java.util.List;

public interface DoctorAvailabilityRepository extends JpaRepository<DoctorAvailability, Long> {

    List<DoctorAvailability> findByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);

    @Query("SELECT da FROM DoctorAvailability da WHERE da.doctor = ?1")
    List<DoctorAvailability> findByDoctor(Doctor doctor);
}

