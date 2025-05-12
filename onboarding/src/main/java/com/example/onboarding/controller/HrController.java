package com.example.onboarding.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.repository.HRRepository;

@RestController
@RequestMapping("/api/hr")
public class HrController {

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private HRRepository hrRepository;

	@PreAuthorize("hasAuthority('HR')")
	@GetMapping("/viewCandidates")
	public ResponseEntity<List<Candidate>> viewAssignedCandidates(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		// Assuming you store HR username as authentication principal
		String username = userDetails.getUsername();

		HR hr = hrRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("HR not found"));

		List<Candidate> candidates = candidateRepository.findByHr(hr);
		return ResponseEntity.ok(candidates);
	}
}
