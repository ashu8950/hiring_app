package com.example.onboarding.dto;

import java.time.LocalDateTime;

import com.example.onboarding.enums.CandidateRole;
import com.example.onboarding.enums.CandidateStatus;
import com.example.onboarding.enums.OnboardStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CandidateDTO {

	// Constructor for CandidateDTO with parameters
	public CandidateDTO(Long id, String name, String email, String phoneNumber, CandidateStatus status,
			OnboardStatus onboardStatus, String documentPath, CandidateRole role, Long hrId, Long managerId,
			Long teamId, Long adminId, LocalDateTime createdAt, LocalDateTime updatedAt, CandidateBankInfoDTO bankInfo,
			CandidateEducationalInfoDTO educationalInfo, CandidatePersonalInfoDTO personalInfo,
			CandidateDocumentDTO document) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.onboardStatus = onboardStatus;
		this.documentPath = documentPath;
		this.role = role;
		this.hrId = hrId;
		this.managerId = managerId;
		this.teamId = teamId;
		this.adminId = adminId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.bankInfo = bankInfo;
		this.educationalInfo = educationalInfo;
		this.personalInfo = personalInfo;
		this.document = document;
	}

	private Long id;

	@NotBlank(message = "Name is required")
	private String name;

	@Email(message = "Email should be valid")
	private String email;

	@Pattern(regexp = "^\\d{10}$", message = "Phone number should be 10 digits")
	private String phoneNumber;

	@NotNull(message = "Status is required")
	private CandidateStatus status;

	@NotNull(message = "Onboarding status is required")
	private OnboardStatus onboardStatus;

	private String documentPath;

	@NotNull(message = "Role is required")
	private CandidateRole role;

	private Long hrId;
	private Long managerId;
	private Long teamId;
	private Long adminId;

	// Related entities (DTO representations of related entities)
	private CandidateBankInfoDTO bankInfo;
	private CandidateEducationalInfoDTO educationalInfo;
	private CandidatePersonalInfoDTO personalInfo;
	private CandidateDocumentDTO document;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
