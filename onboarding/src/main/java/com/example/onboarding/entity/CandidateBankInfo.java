package com.example.onboarding.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
public class CandidateBankInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Bank name is required")
	private String bankName;

	@NotBlank(message = "Account number is required")
	private String accountNumber;

	@NotBlank(message = "IFSC code is required")
	private String ifscCode;

	@OneToOne(mappedBy = "bankInfo")
	@JsonBackReference // This prevents recursion on the child side, ignoring the back-reference during
						// serialization
	private Candidate candidate;
}
