package com.example.onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandidateBankInfoDTO {

	private Long id; // Unique identifier for the Bank Info
	private String bankName; // Name of the bank
	private String accountNumber; // Candidate's bank account number
	private String ifscCode; // IFSC code of the bank branch
	private Long candidateId; // The ID of the candidate associated with this bank info
}
