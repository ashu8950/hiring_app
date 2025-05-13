package com.example.onboarding.service.imp;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.onboarding.entity.OtpToken;
import com.example.onboarding.repository.OtpTokenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

	private final OtpTokenRepository otpRepo;

	@Transactional
	public String generateOtp(String phone) {
		otpRepo.deleteByPhone(phone);
		String otp = String.format("%06d", new Random().nextInt(999999));
		OtpToken token = new OtpToken(null, phone, otp, LocalDateTime.now().plusMinutes(5));
		otpRepo.save(token);
		return otp;
	}

	public boolean verifyOtp(String phone, String otp) {
		Optional<OtpToken> token = otpRepo.findByPhoneAndOtp(phone, otp);
		return token.isPresent() && token.get().getExpiresAt().isAfter(LocalDateTime.now());
	}
}
