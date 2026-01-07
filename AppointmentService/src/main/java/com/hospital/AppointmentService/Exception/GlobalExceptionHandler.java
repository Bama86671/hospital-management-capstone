package com.hospital.AppointmentService.Exception;

import java.time.Instant;
import java.util.*;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	  @ExceptionHandler(MethodArgumentNotValidException.class)
	  public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
	    List<String> errors = ex.getBindingResult().getFieldErrors().stream()
	      .map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();
	    return ResponseEntity.badRequest().body(Map.of(
	      "title", "Validation failed",
	      "status", 400,
	      "errors", errors,
	      "timestamp", Instant.now().toString()
	    ));
	  }

	  @ExceptionHandler({BadRequestException.class, ConflictException.class, NotFoundException.class})
	  public ResponseEntity<Map<String, Object>> business(RuntimeException ex) {
	    int status = ex instanceof BadRequestException ? 400 :
	                 ex instanceof ConflictException ? 409 : 404;
	    return ResponseEntity.status(status).body(Map.of(
	      "title", ex.getMessage(),
	      "status", status,
	      "timestamp", Instant.now().toString()
	    ));
	  }

	  @ExceptionHandler(Exception.class)
	  public ResponseEntity<Map<String, Object>> unknown(Exception ex) {
	    return ResponseEntity.status(500).body(Map.of(
	      "title", "Unexpected error",
	      "status", 500,
	      "timestamp", Instant.now().toString()
	    ));
	  }
	}


