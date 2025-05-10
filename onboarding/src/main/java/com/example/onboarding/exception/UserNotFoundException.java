package com.example.onboarding.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends ApiException {
	public UserNotFoundException(String email) {
		super("User not found with this email" + email);
	}
}
