package com.example.onboarding.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.dto.AuthRequest;
import com.example.onboarding.dto.AuthResponse;
import com.example.onboarding.dto.ForgotPasswordDTO;
import com.example.onboarding.dto.ForgotPasswordEmailDTO;
import com.example.onboarding.dto.ResetPasswordDTO;
import com.example.onboarding.dto.ResetPasswordEmailDTO;
import com.example.onboarding.dto.VerifyOtpDTO;
import com.example.onboarding.dto.VerifyOtpEmailDTO;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.service.RegistrationService;
import com.example.onboarding.service.imp.AuthenticationService;
import com.example.onboarding.service.imp.EmailServiceImpl;
import com.example.onboarding.service.imp.OtpService;
import com.example.onboarding.service.imp.OtpServiceImpl;
import com.example.onboarding.service.imp.SmsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

	private final RegistrationService registrationService;
	private final AuthenticationService authenticationService;
	private final CandidateRepository candidateRepository;
	private final OtpService otpService;
	private final SmsService smsService;
	private final OtpServiceImpl otpServiceImpl;
	private final EmailServiceImpl emailServiceImpl;
	private final PasswordEncoder passwordEncoder;

	// Register API
	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
		return registrationService.register(request);
	}

	// Login API
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
		try {
			AuthResponse response = authenticationService.authenticate(request);
			return ResponseEntity.ok(response);
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	// Forgot Password - Send OTP
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO request) {
		if (!candidateRepository.existsByPhoneNumber(request.getPhone())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone number not registered.");
		}
		String otp = otpService.generateOtp(request.getPhone());
		smsService.sendOtp(request.getPhone(), otp);
		return ResponseEntity.ok("OTP sent to registered phone number.");
	}

	// Verify OTP
	@PostMapping("/verify-otp")
	public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDTO request) {
		boolean valid = otpService.verifyOtp(request.getPhone(), request.getOtp());
		if (valid) {
			return ResponseEntity.ok("OTP verified. You can now reset your password.");
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired OTP.");
	}

	// Reset Password
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request) {
		if (!otpService.verifyOtp(request.getPhone(), request.getOtp())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired OTP.");
		}

		Optional<Candidate> candidateOpt = candidateRepository.findByPhoneNumber(request.getPhone());
		if (candidateOpt.isPresent()) {
			Candidate candidate = candidateOpt.get();
			candidate.setPassword(passwordEncoder.encode(request.getNewPassword()));
			candidateRepository.save(candidate);
			return ResponseEntity.ok("Password has been successfully reset.");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
	}

	// using Email

	@PostMapping("/forgot-password/email")
	public ResponseEntity<?> forgotPasswordByEmail(@Valid @RequestBody ForgotPasswordEmailDTO request) {
		if (!candidateRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not registered.");
		}

		String otp = otpServiceImpl.generateOtp(request.getEmail());
		emailServiceImpl.sendOtp(request.getEmail(), otp);

		return ResponseEntity.ok("OTP sent to registered email.");
	}

	@PostMapping("/verify-otp/email")
	public ResponseEntity<?> verifyOtpByEmail(@Valid @RequestBody VerifyOtpEmailDTO request) {
		boolean valid = otpServiceImpl.verifyOtp(request.getEmail(), request.getOtp());
		if (valid) {
			return ResponseEntity.ok("OTP verified. You can now reset your password.");
		}
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired OTP.");
	}

	@PostMapping("/reset-password/email")
	public ResponseEntity<?> resetPasswordByEmail(@Valid @RequestBody ResetPasswordEmailDTO request) {
		if (!otpServiceImpl.verifyOtp(request.getEmail(), request.getOtp())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired OTP.");
		}

		return candidateRepository.findByEmail(request.getEmail()).map(candidate -> {
			candidate.setPassword(passwordEncoder.encode(request.getNewPassword()));
			candidateRepository.save(candidate);
			return ResponseEntity.ok("Password has been successfully reset.");
		}).orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found."));
	}

}
