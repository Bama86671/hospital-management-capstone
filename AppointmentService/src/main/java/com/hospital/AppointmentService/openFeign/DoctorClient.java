package com.hospital.AppointmentService.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name = "doctor-service", path = "/api/doctors")
public interface DoctorClient {
  @GetMapping("/by-specialization")
  List<DoctorDto> getDoctorsBySpecialization(@RequestParam("specialization") String specialization);

  class DoctorDto {
    public Long id;
    public String name;
    public String specialization;
    public boolean available;
  }
}
