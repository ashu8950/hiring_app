package com.example.onboarding.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;

@Repository
public interface HRRepository extends JpaRepository<HR, Long> {

	Optional<HR> findByUsername(String username);

	List<HR> findByManager(Manager manager);

}
