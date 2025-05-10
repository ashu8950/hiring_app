package com.example.onboarding.util;

import org.springframework.http.ResponseEntity;

import com.example.onboarding.dto.SuccessResponse;

public class ResponseUtil {

	// Utility method to create success response
	public static ResponseEntity<SuccessResponse> createSuccessResponse(String message) {
		SuccessResponse successResponse = new SuccessResponse(message);
		return ResponseEntity.ok(successResponse);
	}
}
