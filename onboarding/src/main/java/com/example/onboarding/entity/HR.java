package com.example.onboarding.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
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
	private String email;

	@NotBlank(message = "Password is required")
	@JsonIgnore
	private String password;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	@JsonIgnore
	private Admin admin;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	@JsonIgnore
	private Manager manager;

	@ManyToOne
	@JoinColumn(name = "team_id")
	@JsonIgnore
	private Team team;

	@OneToMany(mappedBy = "hr", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Candidate> candidates;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	@JsonIgnore
	private Branch branch;

}
