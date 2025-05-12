package com.example.onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDocumentDTO {

	private Long id;
	private String documentType;
	private String fileUrl;
	private Boolean verified;
	private Long candidateId; // Adding candidateId field

}
