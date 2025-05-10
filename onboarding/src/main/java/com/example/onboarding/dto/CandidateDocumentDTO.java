package com.example.onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDocumentDTO {

	private Long id; // Unique identifier for the candidate's document
	private String documentType; // Type of document (e.g., passport, ID card, etc.)
	private String fileUrl; // URL or path where the document is stored
	private Boolean verified; // Whether the document has been verified
	private Long candidateId; // The ID of the candidate associated with this document
}
