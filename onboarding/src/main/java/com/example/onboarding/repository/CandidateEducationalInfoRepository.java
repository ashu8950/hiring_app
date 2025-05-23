package com.example.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.CandidateEducationalInfo;

@Repository
public interface CandidateEducationalInfoRepository extends JpaRepository<CandidateEducationalInfo, Long> {

	CandidateEducationalInfo findByCandidateId(Long candidateId);

	// Add a method to delete educational info by candidate ID
	void deleteByCandidateId(Long candidateId);
}
