package com.example.onboarding.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Min;
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
public class CandidateEducationalInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Highest qualification is required")
	private String highestQualification;

	@NotBlank(message = "University is required")
	private String university;

	@NotNull(message = "Graduation year is required")
	@Min(value = 1900, message = "Graduation year must be valid")
	private Integer graduationYear;

	@OneToOne
	@JoinColumn(name = "candidate_id", referencedColumnName = "id", nullable = false)
	private Candidate candidate;
}
