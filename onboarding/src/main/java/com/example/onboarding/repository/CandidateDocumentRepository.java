package com.example.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.onboarding.entity.CandidateDocument;

public interface CandidateDocumentRepository extends JpaRepository<CandidateDocument, Long> {

	@Modifying
	@Transactional
	@Query("DELETE FROM CandidateDocument cd WHERE cd.candidate.id = :candidateId")
	void deleteByCandidateId(@Param("candidateId") Long candidateId);

	@Query("SELECT cd FROM CandidateDocument cd WHERE cd.candidate.id = :candidateId")
	CandidateDocument findByCandidateId(@Param("candidateId") Long candidateId);
}
