package com.example.onboarding.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.entity.Branch;
import com.example.onboarding.repository.BranchRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
public class BranchController {

	private final BranchRepository branchRepository;

	// Create a new branch
	@PostMapping
	public ResponseEntity<?> createBranch(@RequestBody Branch branch) {
		if (branch.getName() == null || branch.getName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Branch name is required.");
		}

		// Optional: Prevent duplicate names
		Optional<Branch> existingBranch = branchRepository.findAll().stream()
				.filter(b -> b.getName().equalsIgnoreCase(branch.getName())).findFirst();

		if (existingBranch.isPresent()) {
			return ResponseEntity.badRequest().body("A branch with this name already exists.");
		}

		Branch savedBranch = branchRepository.save(branch);
		return ResponseEntity.ok("Branch created successfully with ID: " + savedBranch.getId());
	}

	// Get all branches
	@GetMapping
	public ResponseEntity<?> getAllBranches() {
		List<Branch> branches = branchRepository.findAll();
		if (branches.isEmpty()) {
			return ResponseEntity.ok("No branches found.");
		}
		return ResponseEntity.ok(branches);
	}

	// Get a branch by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getBranchById(@PathVariable Long id) {
		return branchRepository.findById(id).<ResponseEntity<?>>map(branch -> ResponseEntity.ok().body(branch))
				.orElseGet(() -> ResponseEntity.status(404).body("Branch not found with ID: " + id));
	}
}
