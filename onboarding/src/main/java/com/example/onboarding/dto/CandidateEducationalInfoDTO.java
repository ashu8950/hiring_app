package com.example.onboarding.dto;

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
public class CandidateEducationalInfoDTO {

	private Long id;
	private String highestQualification;
	private String university;
	private Integer graduationYear;
	private Long candidateId;
}
