package com.example.onboarding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.enums.CandidateStatus;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	// Find by email
	Optional<Candidate> findByEmail(String email);

	// Find by status
	List<Candidate> findByStatus(CandidateStatus status);

	// Find by status paginated
	Page<Candidate> findByStatus(CandidateStatus status, Pageable pageable);

	List<Candidate> findByHr(HR hr);

	List<Candidate> findByManager(Manager manager);
}
