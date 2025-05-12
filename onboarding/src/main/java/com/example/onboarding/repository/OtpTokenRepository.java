package com.example.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onboarding.entity.OtpToken;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
	Optional<OtpToken> findByPhoneAndOtp(String phone, String otp);
}
