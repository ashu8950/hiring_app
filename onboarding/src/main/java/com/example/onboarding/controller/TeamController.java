package com.example.onboarding.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.entity.Team;
import com.example.onboarding.repository.HRRepository;
import com.example.onboarding.repository.ManagerRepository;
import com.example.onboarding.repository.TeamRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

	private final TeamRepository teamRepository;
	private final HRRepository hrRepository;
	private final ManagerRepository managerRepository;

	// Create a new team
	@PostMapping
	public ResponseEntity<?> createTeam(@RequestBody Team teamRequest) {
		if (teamRequest.getName() == null || teamRequest.getName().trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Team name is required.");
		}

		// Initialize HR and Manager objects
		HR hr = null;
		Manager manager = null;

		// Validate HR if provided
		if (teamRequest.getHr() != null && teamRequest.getHr().getId() != null) {
			hr = hrRepository.findById(teamRequest.getHr().getId()).orElse(null);
			if (hr == null) {
				return ResponseEntity.badRequest().body("HR not found with ID: " + teamRequest.getHr().getId());
			}
		}

		// Validate Manager if provided
		if (teamRequest.getManager() != null && teamRequest.getManager().getId() != null) {
			manager = managerRepository.findById(teamRequest.getManager().getId()).orElse(null);
			if (manager == null) {
				return ResponseEntity.badRequest()
						.body("Manager not found with ID: " + teamRequest.getManager().getId());
			}
		}

		// Enforce rule: if HR is null, Manager must be present
		if (hr == null && manager == null) {
			return ResponseEntity.badRequest().body("Either HR or Manager must be assigned to the team.");
		}

		// Save team
		Team team = new Team();
		team.setName(teamRequest.getName());
		team.setHr(hr);
		team.setManager(manager);

		Team savedTeam = teamRepository.save(team);
		return ResponseEntity.ok("Team created successfully with ID: " + savedTeam.getId());
	}

	// Get all teams
	@GetMapping
	public ResponseEntity<?> getAllTeams() {
		List<Team> teams = teamRepository.findAll();
		if (teams.isEmpty()) {
			return ResponseEntity.ok("No teams found.");
		}
		return ResponseEntity.ok(teams);
	}

	// Get a team by ID
	@GetMapping("/{id}")
	public ResponseEntity<?> getTeamById(@PathVariable Long id) {
		Team team = teamRepository.findById(id).orElse(null);
		if (team == null) {
			return ResponseEntity.badRequest().body("Team not found with ID: " + id);
		}
		return ResponseEntity.ok(team);
	}

}
