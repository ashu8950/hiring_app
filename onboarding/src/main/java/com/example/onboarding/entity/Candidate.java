package com.example.onboarding.entity;

import java.time.LocalDateTime;

import com.example.onboarding.enums.CandidateRole;
import com.example.onboarding.enums.CandidateStatus;
import com.example.onboarding.enums.OnboardStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

	@NotBlank(message = "Name is required")
	@Size(max = 100, message = "Name can be a maximum of 100 characters")
	private String name;

	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is required")
	private String email;

	@Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
	private String phoneNumber;

	@NotNull(message = "Status is required")
	@Enumerated(EnumType.STRING)
	private CandidateStatus status;

	@NotNull(message = "Onboard status is required")
	@Enumerated(EnumType.STRING)
	private OnboardStatus onboardStatus;

	private String documentPath;

	@NotNull(message = "Role is required")
	@Enumerated(EnumType.STRING)
	private CandidateRole role;

	@NotBlank(message = "Password is required")
	@Size(min = 8, message = "Password should be at least 8 characters")
	private String password;

	@ManyToOne
	@JoinColumn(name = "hr_id")
	@JsonBackReference
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

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "bank_info_id")
	@JsonManagedReference // Prevent recursion by managing the relationship serialization
	private CandidateBankInfo bankInfo;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "educational_info_id")
	private CandidateEducationalInfo educationalInfo;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "personal_info_id")
	@JsonManagedReference
	private CandidatePersonalInfo personalInfo;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "candidate_document_id")
	private CandidateDocument document;

	@ManyToOne
	@JoinColumn(name = "branch_id")
	private Branch branch;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
