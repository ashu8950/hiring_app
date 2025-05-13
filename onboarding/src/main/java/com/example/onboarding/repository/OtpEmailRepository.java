package com.example.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.onboarding.entity.OtpEmail;

public interface OtpEmailRepository extends JpaRepository<OtpEmail, Long> {
	Optional<OtpEmail> findByEmail(String email);

	void deleteByEmail(String email);
}
