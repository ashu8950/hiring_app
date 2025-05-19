package com.example.onboarding.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidatePersonalInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@Email
	private String email;

	@NotNull
	private LocalDate dateOfBirth;

	@Pattern(regexp = "^\\d{10}$")
	private String phoneNumber;

	private String gender;
	private String address;
	private String nationality;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "candidate_id", nullable = false, unique = true)
	@JsonBackReference
	private Candidate candidate;
}
