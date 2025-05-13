package com.example.onboarding.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	// View assigned candidates for HR
	@PreAuthorize("hasAuthority('HR')")
	@GetMapping("/viewCandidates")
	public ResponseEntity<?> viewAssignedCandidates(Authentication authentication) {
		String username = ((UserDetails) authentication.getPrincipal()).getUsername();
		Optional<HR> hrOpt = hrRepository.findByUsername(username);

		if (hrOpt.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("HR not found");
		}

		List<Candidate> candidates = candidateRepository.findByHr(hrOpt.get());
		return candidates.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body("No candidates found")
				: ResponseEntity.ok(candidates);
	}
}
