package com.example.onboarding.exception;

public class CandidateNotFoundException extends RuntimeException {
	public CandidateNotFoundException(String message) {
		super(message);
	}
}