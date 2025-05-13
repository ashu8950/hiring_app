package com.example.onboarding.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	private String email;

	@NotBlank(message = "Password is required")
	@JsonIgnore
	private String password;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	@NotNull(message = "Admin ID is required")
	@JsonIgnore
	private Admin admin;

	@ManyToOne
	@JoinColumn(name = "team_id")
	@JsonIgnore
	private Team team;

	@OneToMany(mappedBy = "manager")
	@JsonIgnore
	private List<HR> hrs;

	@OneToMany(mappedBy = "manager")
	@JsonIgnore
	private List<Candidate> candidates;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "branch_id")
	@JsonIgnore
	private Branch branch;

}
