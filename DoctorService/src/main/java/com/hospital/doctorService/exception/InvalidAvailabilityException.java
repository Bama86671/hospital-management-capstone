package com.hospital.doctorService.exception;

public class InvalidAvailabilityException extends RuntimeException {
    public InvalidAvailabilityException(String message) {
        super(message);
    }
}
