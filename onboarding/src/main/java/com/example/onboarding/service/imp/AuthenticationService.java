package com.example.onboarding.service.imp;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.onboarding.config.JwtTokenProvider;
import com.example.onboarding.dto.AuthRequest;
import com.example.onboarding.dto.AuthResponse;
import com.example.onboarding.entity.Admin;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.enums.Role;
import com.example.onboarding.repository.AdminRepository;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.repository.HRRepository;
import com.example.onboarding.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final AdminRepository adminRepository;
	private final HRRepository hrRepository;
	private final ManagerRepository managerRepository;
	private final CandidateRepository candidateRepository;

	public AuthResponse authenticate(AuthRequest request) {
		log.info("Attempting to authenticate user: {}", request.getUsername());

		// Perform authentication using Spring Security
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		String token;
		String email;
		Role role;

		// Find the user in role-specific tables
		if ((request.getRole() == Role.ADMIN)) {
			Admin admin = adminRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
			email = admin.getEmail();
			role = Role.ADMIN;
		} else if (request.getRole() == Role.HR) {
			HR hr = hrRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("HR not found"));
			email = hr.getEmail();
			role = Role.HR;
		} else if (request.getRole() == Role.MANAGER) {
			Manager manager = managerRepository.findByUsername(request.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("Manager not found"));
			email = manager.getEmail();
			role = Role.MANAGER;
		} else if (request.getRole() == Role.CANDIDATE) {
			Candidate candidate = candidateRepository.findByName(request.getUsername())
					.orElseThrow(() -> new UsernameNotFoundException("Candidate not found"));
			email = candidate.getEmail();
			role = Role.CANDIDATE;
		} else {
			throw new UsernameNotFoundException("Invalid role");
		}
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		log.info(userDetails.getPassword());
		log.info(userDetails.getUsername());
		token = jwtTokenProvider.generateToken(userDetails);

		return new AuthResponse(token, request.getUsername(), email, role.name());
	}
}
