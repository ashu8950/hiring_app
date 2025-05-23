package com.example.onboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	Team findByName(String name);
}
