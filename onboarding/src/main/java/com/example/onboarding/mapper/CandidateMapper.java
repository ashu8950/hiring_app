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
		if (candidate == null) {
			return null;
		}

		return new CandidateDTO(candidate.getId(), candidate.getName(), candidate.getEmail(),
				candidate.getPhoneNumber(), candidate.getStatus(), candidate.getOnboardStatus(),
				candidate.getDocumentPath(), candidate.getRole(), getHrId(candidate), getManagerId(candidate),
				getTeamId(candidate), getAdminId(candidate), candidate.getCreatedAt(), candidate.getUpdatedAt(),
				mapBankInfo(candidate.getBankInfo()), mapEducationalInfo(candidate.getEducationalInfo()),
				mapPersonalInfo(candidate.getPersonalInfo()), mapDocument(candidate.getDocument()));
	}

	// Convert from CandidateDTO to Candidate entity (basic fields)
	public static Candidate toEntity(CandidateDTO dto) {
		if (dto == null) {
			return null;
		}

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

	// Convert from CandidateDTO to Candidate entity with relations
	public static Candidate toEntityWithRelations(CandidateDTO dto, HR hr, Manager manager, Team team, Admin admin) {
		Candidate candidate = toEntity(dto);
		candidate.setHr(hr);
		candidate.setManager(manager);
		candidate.setTeam(team);
		candidate.setAdmin(admin);
		return candidate;
	}

	// Helper to map CandidateBankInfo entity to CandidateBankInfoDTO
	private static CandidateBankInfoDTO mapBankInfo(CandidateBankInfo bankInfo) {
		if (bankInfo == null) {
			return null;
		}

		return new CandidateBankInfoDTO(bankInfo.getId(), bankInfo.getBankName(), bankInfo.getAccountNumber(),
				bankInfo.getIfscCode(), getCandidateId(bankInfo));
	}

	// Helper to map CandidateEducationalInfo entity to CandidateEducationalInfoDTO
	private static CandidateEducationalInfoDTO mapEducationalInfo(CandidateEducationalInfo educationalInfo) {
		if (educationalInfo == null) {
			return null;
		}

		return new CandidateEducationalInfoDTO(educationalInfo.getId(), educationalInfo.getHighestQualification(),
				educationalInfo.getUniversity(), educationalInfo.getGraduationYear(), getCandidateId(educationalInfo));
	}

	// Helper to map CandidatePersonalInfo entity to CandidatePersonalInfoDTO
	private static CandidatePersonalInfoDTO mapPersonalInfo(CandidatePersonalInfo personalInfo) {
		if (personalInfo == null) {
			return null;
		}

		return new CandidatePersonalInfoDTO(personalInfo.getId(), personalInfo.getName(), personalInfo.getEmail(),
				personalInfo.getDateOfBirth(), personalInfo.getPhoneNumber(), personalInfo.getGender(),
				personalInfo.getAddress(), personalInfo.getNationality(), getCandidateId(personalInfo));
	}

	// Helper to map CandidateDocument entity to CandidateDocumentDTO
	private static CandidateDocumentDTO mapDocument(CandidateDocument document) {
		if (document == null) {
			return null;
		}

		return new CandidateDocumentDTO(document.getId(), document.getDocumentType(), document.getFileUrl(),
				document.getVerified(), getCandidateId(document));
	}

	// Helper to safely get Candidate ID from related entities to prevent
	// NullPointerException
	private static Long getCandidateId(Object entity) {
		if (entity == null) {
			return null;
		}

		if (entity instanceof CandidateBankInfo) {
			return ((CandidateBankInfo) entity).getCandidate() != null
					? ((CandidateBankInfo) entity).getCandidate().getId()
					: null;
		} else if (entity instanceof CandidateEducationalInfo) {
			return ((CandidateEducationalInfo) entity).getCandidate() != null
					? ((CandidateEducationalInfo) entity).getCandidate().getId()
					: null;
		} else if (entity instanceof CandidatePersonalInfo) {
			return ((CandidatePersonalInfo) entity).getCandidate() != null
					? ((CandidatePersonalInfo) entity).getCandidate().getId()
					: null;
		} else if (entity instanceof CandidateDocument) {
			return ((CandidateDocument) entity).getCandidate() != null
					? ((CandidateDocument) entity).getCandidate().getId()
					: null;
		}

		return null;
	}

	// Helper to get HR ID from Candidate (if present)
	private static Long getHrId(Candidate candidate) {
		return candidate.getHr() != null ? candidate.getHr().getId() : null;
	}

	// Helper to get Manager ID from Candidate (if present)
	private static Long getManagerId(Candidate candidate) {
		return candidate.getManager() != null ? candidate.getManager().getId() : null;
	}

	// Helper to get Team ID from Candidate (if present)
	private static Long getTeamId(Candidate candidate) {
		return candidate.getTeam() != null ? candidate.getTeam().getId() : null;
	}

	// Helper to get Admin ID from Candidate (if present)
	private static Long getAdminId(Candidate candidate) {
		return candidate.getAdmin() != null ? candidate.getAdmin().getId() : null;
	}
}
