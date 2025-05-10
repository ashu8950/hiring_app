package com.example.onboarding.service.imp;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.onboarding.dto.AuthRequest;
import com.example.onboarding.entity.Admin;
import com.example.onboarding.entity.Branch;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.entity.Team;
import com.example.onboarding.enums.CandidateRole;
import com.example.onboarding.enums.CandidateStatus;
import com.example.onboarding.enums.OnboardStatus;
import com.example.onboarding.enums.Role;
import com.example.onboarding.repository.AdminRepository;
import com.example.onboarding.repository.BranchRepository;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.repository.HRRepository;
import com.example.onboarding.repository.ManagerRepository;
import com.example.onboarding.repository.TeamRepository;
import com.example.onboarding.service.RegistrationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

	private final AdminRepository adminRepository;
	private final HRRepository hrRepository;
	private final ManagerRepository managerRepository;
	private final CandidateRepository candidateRepository;
	private final TeamRepository teamRepository;
	private final BranchRepository branchRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public ResponseEntity<?> register(AuthRequest request) {
		// Handle Admin Registration (Only one Admin allowed)
		if (request.getRole() == Role.ADMIN) {
			if (adminRepository.count() > 0) {
				return ResponseEntity.badRequest().body("Only one Admin is allowed.");
			}
			Admin admin = new Admin();
			admin.setUsername(request.getUsername());
			admin.setEmail(request.getEmail());
			admin.setPassword(passwordEncoder.encode(request.getPassword()));
			adminRepository.save(admin);
			return ResponseEntity.ok("Admin registered successfully.");
		}

		// Handle HR Registration (HR must be assigned to a Team)
		if (request.getRole() == Role.HR) {
			if (request.getTeamId() == null) {
				return ResponseEntity.badRequest().body("Team ID is required for HR registration.");
			}

			Team team = teamRepository.findById(request.getTeamId()).orElse(null);
			if (team == null) {
				return ResponseEntity.badRequest().body("Team not found.");
			}

			HR hr = new HR();
			hr.setUsername(request.getUsername());
			hr.setEmail(request.getEmail());
			hr.setPassword(passwordEncoder.encode(request.getPassword()));
			hr.setTeam(team); // Assigning the team to the HR
			hrRepository.save(hr);

			return ResponseEntity.ok("HR registered successfully.");
		}

		// Handle Manager Registration (Manager is assigned to a branch)
		if (request.getRole() == Role.MANAGER) {
			if (request.getBranchId() == null) {
				return ResponseEntity.badRequest().body("Branch ID is required for Manager role.");
			}

			Branch branch = branchRepository.findById(request.getBranchId()).orElse(null);
			if (branch == null) {
				return ResponseEntity.badRequest().body("Branch not found.");
			}

			Admin admin = adminRepository.findAll().stream().findFirst().orElse(null);
			if (admin == null) {
				return ResponseEntity.badRequest().body("Admin must be created before registering a Manager.");
			}
			Manager manager = new Manager();
			manager.setUsername(request.getUsername());
			manager.setEmail(request.getEmail());
			manager.setPassword(passwordEncoder.encode(request.getPassword()));
			manager.setBranch(branch);
			manager.setAdmin(admin);
			managerRepository.save(manager);

			return ResponseEntity.ok("Manager registered successfully.");
		}

		// Handle Candidate Registration (Candidate is assigned to an HR and Manager)
		if (request.getRole() == Role.CANDIDATE) {

			// Ensure at least one of HR ID or Manager ID is provided
			if (request.getHrId() == null && request.getManagerId() == null) {
				return ResponseEntity.badRequest().body("Either HR ID or Manager ID must be provided.");
			}

			Candidate candidate = new Candidate();
			candidate.setName(request.getUsername());
			candidate.setEmail(request.getEmail());
			candidate.setPassword(passwordEncoder.encode(request.getPassword()));
			candidate.setRole(CandidateRole.valueOf(request.getCandidateRole()));
			candidate.setOnboardStatus(OnboardStatus.IN_PROGRESS);
			candidate.setStatus(CandidateStatus.ACTIVE);

			// Assign HR if provided
			if (request.getHrId() != null) {
				HR hr = hrRepository.findById(request.getHrId()).orElse(null);
				if (hr == null) {
					return ResponseEntity.badRequest().body("HR not found.");
				}
				candidate.setHr(hr);
			}

			// Assign Manager if provided
			if (request.getManagerId() != null) {
				Manager manager = managerRepository.findById(request.getManagerId()).orElse(null);
				if (manager == null) {
					return ResponseEntity.badRequest().body("Manager not found.");
				}
				candidate.setManager(manager);
			}

			candidateRepository.save(candidate);
			return ResponseEntity.ok("Candidate registered successfully.");
		}

		return ResponseEntity.badRequest().body("Invalid role.");
	}
}
