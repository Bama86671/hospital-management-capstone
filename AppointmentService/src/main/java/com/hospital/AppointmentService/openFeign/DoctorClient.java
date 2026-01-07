package com.hospital.AppointmentService.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "DoctorService", path = "/api/doctors")
public interface DoctorClient {
  @GetMapping("/by-specialization")
  List<DoctorDto> getDoctorsBySpecialization(@RequestParam("specialization") String specialization);
  
  @GetMapping("/availability") 
  AvailabilityDto checkAvailability(@RequestParam("doctorId") Long doctorId, 
		  @RequestParam("from") String from, 
		  @RequestParam("to") String to);

  
  class DoctorDto {
    public Long id;
    public String name;
    public String specialization;
    public boolean available;
  }
  class AvailabilityDto { 
	  public boolean available; 
	  public String message;
	  }
  
}
