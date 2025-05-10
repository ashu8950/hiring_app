package com.example.onboarding.service;

import org.springframework.http.ResponseEntity;

import com.example.onboarding.dto.AuthRequest;

public interface RegistrationService {
	ResponseEntity<?> register(AuthRequest request);
}
