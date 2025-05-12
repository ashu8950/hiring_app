package com.example.onboarding.mapper;

import com.example.onboarding.dto.CandidateDocumentDTO;
import com.example.onboarding.entity.CandidateDocument;

public class CandidateDocumentMapper {

	// Convert CandidateDocument entity to CandidateDocumentDTO
	public static CandidateDocumentDTO toDTO(CandidateDocument document) {
		if (document == null) {
			return null;
		}

		return new CandidateDocumentDTO(document.getId(), document.getDocumentType(), document.getFileUrl(),
				document.getVerified(), document.getCandidate() != null ? document.getCandidate().getId() : null // Linking
																													// candidateId
		);
	}

	// Convert CandidateDocumentDTO to CandidateDocument entity
	public static CandidateDocument toEntity(CandidateDocumentDTO dto) {
		if (dto == null) {
			return null;
		}

		CandidateDocument entity = new CandidateDocument();
		entity.setId(dto.getId());
		entity.setDocumentType(dto.getDocumentType());
		entity.setFileUrl(dto.getFileUrl());
		entity.setVerified(dto.getVerified());

		return entity;
	}
}
