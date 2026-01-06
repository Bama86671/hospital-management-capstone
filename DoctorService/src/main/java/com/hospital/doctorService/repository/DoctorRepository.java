package com.hospital.doctorService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hospital.doctorService.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long>{

	boolean existsByEmail(String email);
}
