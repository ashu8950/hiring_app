package com.example.onboarding.entity;

import java.time.LocalDateTime;

import com.example.onboarding.enums.CandidateRole;
import com.example.onboarding.enums.CandidateStatus;
import com.example.onboarding.enums.OnboardStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 100)
	private String name;

	@Email
	@NotBlank
	private String email;

	@Pattern(regexp = "^\\d{10}$")
	private String phoneNumber;

	@NotNull
	@Enumerated(EnumType.STRING)
	private CandidateStatus status;

	@NotNull
	@Enumerated(EnumType.STRING)
	private OnboardStatus onboardStatus;

	@NotNull
	@Enumerated(EnumType.STRING)
	private CandidateRole role;

	@NotBlank
	@Size(min = 8)
	private String password;

	private String documentPath;

	@ManyToOne
	@JoinColumn(name = "hr_id")
	@JsonIgnore
	private HR hr;

	@ManyToOne
	@JoinColumn(name = "manager_id")
	@JsonIgnore
	private Manager manager;

	@ManyToOne
	@JoinColumn(name = "team_id")
	@JsonIgnore
	private Team team;

	@ManyToOne
	@JoinColumn(name = "admin_id")
	@JsonIgnore
	private Admin admin;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	@JsonIgnore
	private Branch branch;

	// One-to-one children, cascade & orphanRemoval
	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private CandidateBankInfo bankInfo;

	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private CandidateEducationalInfo educationalInfo;

	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private CandidatePersonalInfo personalInfo;

	@OneToOne(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private CandidateDocument document;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
