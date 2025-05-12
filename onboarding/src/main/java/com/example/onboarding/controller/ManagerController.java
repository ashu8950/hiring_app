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
import com.example.onboarding.entity.Manager;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.repository.HRRepository;
import com.example.onboarding.repository.ManagerRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/manager")
@Slf4j
public class ManagerController {

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private HRRepository hrRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@PreAuthorize("hasAuthority('MANAGER')")
	@GetMapping("/viewCandidates")
	public ResponseEntity<?> viewTeamCandidates(Authentication authentication) {
		log.info(authentication.getName());
		try {
			String username = ((UserDetails) authentication.getPrincipal()).getUsername();
			log.info(username);
			Manager manager = managerRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Manager not found"));

			List<Candidate> candidates = candidateRepository.findByManagerId(manager.getId());
			return ResponseEntity.ok(candidates);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error fetching candidates: " + e.getMessage());
		}
	}

	@PreAuthorize("hasAuthority('MANAGER')")
	@GetMapping("/viewHRs")
	public ResponseEntity<?> viewAssignedHRs(Authentication authentication) {
		try {
			String username = ((UserDetails) authentication.getPrincipal()).getUsername();
			Manager manager = managerRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("Manager not found"));

			List<HR> hrs = hrRepository.findByManager(manager);
			return ResponseEntity.ok(hrs);
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error fetching HRs: " + e.getMessage());
		}
	}
}
