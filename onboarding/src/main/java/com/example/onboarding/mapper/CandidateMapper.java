package com.example.onboarding.mapper;

import com.example.onboarding.dto.CandidateBankInfoDTO;
import com.example.onboarding.dto.CandidateDTO;
import com.example.onboarding.dto.CandidateDocumentDTO;
import com.example.onboarding.dto.CandidateEducationalInfoDTO;
import com.example.onboarding.dto.CandidatePersonalInfoDTO;
import com.example.onboarding.entity.Admin;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.CandidateBankInfo;
import com.example.onboarding.entity.CandidateDocument;
import com.example.onboarding.entity.CandidateEducationalInfo;
import com.example.onboarding.entity.CandidatePersonalInfo;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.entity.Team;

public class CandidateMapper {

	// Convert from Candidate entity to CandidateDTO
	public static CandidateDTO toDTO(Candidate candidate) {
		CandidateDTO candidateDTO = new CandidateDTO(candidate.getId(), candidate.getName(), candidate.getEmail(),
				candidate.getPhoneNumber(), candidate.getStatus(), candidate.getOnboardStatus(),
				candidate.getDocumentPath(), candidate.getRole(),
				candidate.getHr() != null ? candidate.getHr().getId() : null,
				candidate.getManager() != null ? candidate.getManager().getId() : null,
				candidate.getTeam() != null ? candidate.getTeam().getId() : null,
				candidate.getAdmin() != null ? candidate.getAdmin().getId() : null, candidate.getCreatedAt(),
				candidate.getUpdatedAt(), candidate.getBankInfo() != null ? mapBankInfo(candidate.getBankInfo()) : null,
				candidate.getEducationalInfo() != null ? mapEducationalInfo(candidate.getEducationalInfo()) : null,
				candidate.getPersonalInfo() != null ? mapPersonalInfo(candidate.getPersonalInfo()) : null,
				candidate.getDocument() != null ? mapDocument(candidate.getDocument()) : null);
		return candidateDTO;
	}

	// Convert from CandidateDTO to Candidate entity (without related entities)
	public static Candidate toEntity(CandidateDTO dto) {
		Candidate candidate = new Candidate();
		candidate.setId(dto.getId());
		candidate.setName(dto.getName());
		candidate.setEmail(dto.getEmail());
		candidate.setPhoneNumber(dto.getPhoneNumber());
		candidate.setStatus(dto.getStatus());
		candidate.setOnboardStatus(dto.getOnboardStatus());
		candidate.setDocumentPath(dto.getDocumentPath());
		candidate.setRole(dto.getRole());
		candidate.setCreatedAt(dto.getCreatedAt());
		candidate.setUpdatedAt(dto.getUpdatedAt());

		return candidate;
	}

	// Convert from CandidateDTO to Candidate entity with relations (hr, manager,
	// team, admin)
	public static Candidate toEntityWithRelations(CandidateDTO dto, HR hr, Manager manager, Team team, Admin admin) {
		Candidate candidate = toEntity(dto);
		candidate.setHr(hr);
		candidate.setManager(manager);
		candidate.setTeam(team);
		candidate.setAdmin(admin);
		return candidate;
	}

	// Map CandidateBankInfo entity to CandidateBankInfoDTO
	private static CandidateBankInfoDTO mapBankInfo(CandidateBankInfo bankInfo) {
		if (bankInfo != null) {
			return new CandidateBankInfoDTO(bankInfo.getId(), bankInfo.getBankName(), bankInfo.getAccountNumber(),
					bankInfo.getIfscCode(), bankInfo.getCandidate() != null ? bankInfo.getCandidate().getId() : null);
		}
		return null;
	}

	// Map CandidateEducationalInfo entity to CandidateEducationalInfoDTO
	private static CandidateEducationalInfoDTO mapEducationalInfo(CandidateEducationalInfo educationalInfo) {
		if (educationalInfo != null) {
			return new CandidateEducationalInfoDTO(educationalInfo.getId(), educationalInfo.getHighestQualification(),
					educationalInfo.getUniversity(), educationalInfo.getGraduationYear(),
					educationalInfo.getCandidate() != null ? educationalInfo.getCandidate().getId() : null);
		}
		return null;
	}

	// Map CandidatePersonalInfo entity to CandidatePersonalInfoDTO
	private static CandidatePersonalInfoDTO mapPersonalInfo(CandidatePersonalInfo personalInfo) {
		if (personalInfo != null) {
			return new CandidatePersonalInfoDTO(personalInfo.getId(), personalInfo.getName(), personalInfo.getEmail(),
					personalInfo.getDateOfBirth(), personalInfo.getPhoneNumber(), personalInfo.getGender(),
					personalInfo.getAddress(), personalInfo.getNationality(),
					personalInfo.getCandidate() != null ? personalInfo.getCandidate().getId() : null);
		}
		return null;
	}

	// Map CandidateDocument entity to CandidateDocumentDTO
	private static CandidateDocumentDTO mapDocument(CandidateDocument document) {
		if (document != null) {
			return new CandidateDocumentDTO(document.getId(), document.getDocumentType(), document.getFileUrl(),
					document.getVerified(), document.getCandidate() != null ? document.getCandidate().getId() : null);
		}
		return null;
	}
}
