package com.example.onboarding.mapper;

import com.example.onboarding.dto.CandidateEducationalInfoDTO;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.CandidateEducationalInfo;

public class CandidateEducationalInfoMapper {

	public static CandidateEducationalInfoDTO toDTO(CandidateEducationalInfo entity) {
		if (entity == null)
			return null;
		return new CandidateEducationalInfoDTO(entity.getId(), entity.getHighestQualification(), entity.getUniversity(),
				entity.getGraduationYear(), entity.getCandidate() != null ? entity.getCandidate().getId() : null);
	}

	public static CandidateEducationalInfo toEntity(CandidateEducationalInfoDTO dto, Candidate candidate) {
		if (dto == null) {
			return null;
		}
		CandidateEducationalInfo entity = new CandidateEducationalInfo();
		entity.setId(dto.getId());
		entity.setHighestQualification(dto.getHighestQualification());
		entity.setUniversity(dto.getUniversity());
		entity.setGraduationYear(dto.getGraduationYear());

		// Ensure the candidate reference is set before saving
		if (candidate != null) {
			entity.setCandidate(candidate);
		}

		return entity;
	}

}
