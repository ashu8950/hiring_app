package com.example.onboarding.mapper;

import com.example.onboarding.dto.CandidateBankInfoDTO;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.CandidateBankInfo;

public class CandidateBankInfoMapper {

	public static CandidateBankInfoDTO toDTO(CandidateBankInfo entity) {
		if (entity == null) {
			return null;
		}
		return new CandidateBankInfoDTO(entity.getId(), entity.getBankName(), entity.getAccountNumber(),
				entity.getIfscCode(), entity.getCandidate() != null ? entity.getCandidate().getId() : null);
	}

	public static CandidateBankInfo toEntity(CandidateBankInfoDTO dto, Candidate candidate) {
		if (dto == null) {
			return null;
		}
		CandidateBankInfo entity = new CandidateBankInfo();
		entity.setId(dto.getId());
		entity.setBankName(dto.getBankName());
		entity.setAccountNumber(dto.getAccountNumber());
		entity.setIfscCode(dto.getIfscCode());

		// Set the candidate for the bank info
		entity.setCandidate(candidate);

		return entity;
	}
}
