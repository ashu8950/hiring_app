package com.example.onboarding.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HR {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Username is required")
	private String username;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email; // HR's email field

	@NotBlank(message = "Password is required")
	private String password; // HR's password field

	@ManyToOne
	@JoinColumn(name = "admin_id")
	private Admin admin; // Each HR is associated with an Admin

	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Manager manager; // HR is associated with a single Manager (Manager can have multiple HRs)

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team; // HR is assigned to a Team (optional)

	@OneToMany(mappedBy = "hr")
	private List<Candidate> candidates; // HR manages multiple Candidates

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private Branch branch; // HR is associated with a Branch

}
