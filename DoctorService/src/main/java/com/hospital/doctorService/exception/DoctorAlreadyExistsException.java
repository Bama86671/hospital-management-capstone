package com.hospital.doctorService.exception;


public class DoctorAlreadyExistsException extends RuntimeException {
    public DoctorAlreadyExistsException(String message) {
        super(message);
    }
}
