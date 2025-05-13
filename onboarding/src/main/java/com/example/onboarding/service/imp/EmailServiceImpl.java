package com.example.onboarding.service.imp;

import org.springframework.stereotype.Service;

import com.example.onboarding.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Override
	public void sendOtp(String email, String otp) {

		System.out.println("Sending OTP to email: " + email + " | OTP: " + otp);
		// Integrate with SendGrid, JavaMail, etc.
	}
}
