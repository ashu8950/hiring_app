package com.example.onboarding.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidatePersonalInfoDTO {

	private Long id; // Unique identifier for the personal information
	private String name; // Candidate's full name
	private String email; // Candidate's email address
	private LocalDate dateOfBirth; // Candidate's date of birth
	private String phoneNumber; // Candidate's phone number
	private String gender; // Candidate's gender
	private String address; // Candidate's home address
	private String nationality; // Candidate's nationality
	private Long candidateId; // The ID of the candidate associated with this personal info
}