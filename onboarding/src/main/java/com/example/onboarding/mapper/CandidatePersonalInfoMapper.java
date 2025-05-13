package com.example.onboarding.mapper;

import com.example.onboarding.dto.CandidatePersonalInfoDTO;
import com.example.onboarding.entity.CandidatePersonalInfo;

public class CandidatePersonalInfoMapper {
	public static CandidatePersonalInfoDTO toDTO(CandidatePersonalInfo updated) {
		if (updated == null)
			return null;
		return new CandidatePersonalInfoDTO(updated.getId(), updated.getName(), updated.getEmail(),
				updated.getDateOfBirth(), updated.getPhoneNumber(), updated.getGender(), updated.getAddress(),
				updated.getNationality(), updated.getCandidate() != null ? updated.getCandidate().getId() : null);
	}

	public static CandidatePersonalInfo toEntity(CandidatePersonalInfoDTO dto) {
		if (dto == null)
			return null;
		CandidatePersonalInfo entity = new CandidatePersonalInfo();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setDateOfBirth(dto.getDateOfBirth());
		entity.setPhoneNumber(dto.getPhoneNumber());
		entity.setGender(dto.getGender());
		entity.setAddress(dto.getAddress());
		entity.setNationality(dto.getNationality());

		return entity;
	}
}
