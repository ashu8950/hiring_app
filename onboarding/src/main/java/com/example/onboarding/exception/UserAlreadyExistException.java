package com.example.onboarding.exception;

@SuppressWarnings("serial")
public class UserAlreadyExistException extends ApiException {

	public UserAlreadyExistException(String email) {
		super("User already exist with this mail" + email);
	}
}
