package com.example.onboarding.service.imp;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SmsService {

	public void sendOtp(String to, String otp) {
		// Use Twilio or mock SMS
		log.info("Sending OTP " + otp + " to phone: " + to);
	}
}
