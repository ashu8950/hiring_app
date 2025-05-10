package com.example.onboarding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.repository.HRRepository;
import com.example.onboarding.repository.ManagerRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private HRRepository hrRepository;

	@Autowired
	private CandidateRepository candidateRepository;

	@PostMapping("/assignManager")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> assignManager(@RequestParam Long candidateId, @RequestParam Long managerId) {
		Candidate candidate = candidateRepository.findById(candidateId)
				.orElseThrow(() -> new RuntimeException("Candidate not found"));

		Manager manager = managerRepository.findById(managerId)
				.orElseThrow(() -> new RuntimeException("Manager not found"));

		candidate.setManager(manager);
		candidateRepository.save(candidate);

		return ResponseEntity.ok("Manager " + manager.getUsername() + " assigned to candidate " + candidate.getName());
	}

	@PostMapping("/assignHR")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> assignHR(@RequestParam Long candidateId, @RequestParam Long hrId) {
		Candidate candidate = candidateRepository.findById(candidateId)
				.orElseThrow(() -> new RuntimeException("Candidate not found"));

		HR hr = hrRepository.findById(hrId).orElseThrow(() -> new RuntimeException("HR not found"));

		candidate.setHr(hr);
		candidateRepository.save(candidate);

		return ResponseEntity.ok("HR " + hr.getUsername() + " assigned to candidate " + candidate.getName());
	}
}
