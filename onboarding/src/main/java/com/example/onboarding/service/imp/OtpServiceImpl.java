package com.example.onboarding.service.imp;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.onboarding.entity.OtpEmail;
import com.example.onboarding.repository.OtpEmailRepository;
import com.example.onboarding.service.OtpService;

import jakarta.transaction.Transactional;

@Service
public class OtpServiceImpl implements OtpService {

	private final OtpEmailRepository otpEmailRepository;

	public OtpServiceImpl(OtpEmailRepository otpEmailRepository) {
		this.otpEmailRepository = otpEmailRepository;
	}

	@Override
	@Transactional
	public String generateOtp(String email) {
		// 1. Generate a 6-digit OTP
		String otp = String.valueOf(new Random().nextInt(900000) + 100000);
		LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);

		// 2. Remove any existing OTP for the email (only one active OTP at a time)
		otpEmailRepository.deleteByEmail(email);

		// 3. Save new OTP in the database with expiry time
		OtpEmail otpEntity = new OtpEmail();
		otpEntity.setEmail(email);
		otpEntity.setOtp(otp);
		otpEntity.setExpiryTime(expiryTime);
		otpEmailRepository.save(otpEntity);

		return otp;
	}

	@Override
	@Transactional
	public boolean verifyOtp(String email, String otp) {
		Optional<OtpEmail> otpRecord = otpEmailRepository.findByEmail(email);
		if (otpRecord.isPresent()) {
			OtpEmail storedOtp = otpRecord.get();

			// 1. Match OTP and check expiry
			if (storedOtp.getOtp().equals(otp) && storedOtp.getExpiryTime().isAfter(LocalDateTime.now())) {
				// OTP is valid and not expired
				return true;
			}
		}
		return false; // OTP is invalid or expired
	}
}
