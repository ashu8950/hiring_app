package com.example.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.CandidateBankInfo;

@Repository
public interface CandidateBankInfoRepository extends JpaRepository<CandidateBankInfo, Long> {

	CandidateBankInfo findByCandidateId(Long candidateId);

	// Add a method to delete bank info by candidate ID
	void deleteByCandidateId(Long candidateId);
}
