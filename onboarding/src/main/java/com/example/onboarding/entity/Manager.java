package com.example.onboarding.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manager {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Username is required")
	private String username;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email; // Manager's email field

	@NotBlank(message = "Password is required")
	private String password; // Manager's password field

	// A manager is associated with one admin (only one admin can manage multiple
	// managers)
	@ManyToOne
	@JoinColumn(name = "admin_id")
	@NotNull(message = "Admin ID is required")
	private Admin admin;

	// A manager is assigned to a team
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team; // Manager is assigned to a team (optional or could be set later)

	// A manager can have multiple HRs
	@OneToMany(mappedBy = "manager")
	private List<HR> hrs;

	// A manager can have multiple candidates (these candidates are associated with
	// the manager)
	@OneToMany(mappedBy = "manager")
	private List<Candidate> candidates;

	// A manager is assigned to a branch
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id")
	private Branch branch;

}
