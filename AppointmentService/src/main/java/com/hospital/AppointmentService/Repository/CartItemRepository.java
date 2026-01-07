package com.hospital.AppointmentService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.hospital.AppointmentService.Entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> { 
	List<CartItem> findByPatientId(Long patientId); }