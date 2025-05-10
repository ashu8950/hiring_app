package com.example.onboarding.dto;

import com.example.onboarding.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

	@NotBlank(message = "Username is required")
	private String username;

	@NotBlank(message = "Password is required")
	private String password;

	@Email(message = "Email should be valid")
	private String email;

	private String candidateRole;

	private Role role; // Add role to the request (Admin, HR, Manager, Candidate)
	private Long teamId; // For Manager role, required to assign manager to a team
	private Long hrId; // For Candidate, HR to assign candidate to HR
	private Long managerId;
	private Long branchId;
	private Long adminId;
}
