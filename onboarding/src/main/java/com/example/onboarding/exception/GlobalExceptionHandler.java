package com.example.onboarding.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.example.onboarding.dto.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// Handle all unhandled exceptions
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
		logger.error("Unexpected error", ex);
		return new ResponseEntity<>(new ErrorResponse("Internal Server Error", "Something went wrong."),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// Handle API-specific exceptions
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ErrorResponse> handleApiException(ApiException ex) {
		logger.error("API Exception", ex);
		return ResponseEntity.badRequest().body(new ErrorResponse("API Error", ex.getMessage()));
	}

	@ExceptionHandler(CandidateNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleCandidateNotFound(CandidateNotFoundException ex) {
		logger.warn("Candidate Not Found", ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("Candidate Not Found", ex.getMessage()));
	}

	@ExceptionHandler(InvalidStatusException.class)
	public ResponseEntity<ErrorResponse> handleInvalidStatus(InvalidStatusException ex) {
		logger.warn("Invalid Status", ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse("Invalid Status Transition", ex.getMessage()));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
		logger.warn("User not found", ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse("Authentication Failed", ex.getMessage()));
	}

	// Handle validation errors on request bodies (e.g. @Valid)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

		return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", errors.toString()));
	}

	// Handle validation errors from query parameters, path variables, etc.
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
		logger.warn("Constraint violation", ex);
		return ResponseEntity.badRequest().body(new ErrorResponse("Validation Error", ex.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
		logger.warn("Resource Not Found", ex);
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse("Resource Not Found", ex.getMessage()));
	}

}
