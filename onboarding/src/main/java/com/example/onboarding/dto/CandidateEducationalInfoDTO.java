package com.example.onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateEducationalInfoDTO {

	private Long id; // Unique identifier for the educational information
	private String highestQualification; // The highest qualification of the candidate (e.g., Bachelor's, Master's)
	private String university; // The name of the university where the candidate studied
	private Integer graduationYear; // The year in which the candidate graduated
	private Long candidateId; // The ID of the candidate associated with this educational info
}
