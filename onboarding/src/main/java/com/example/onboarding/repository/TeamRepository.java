package com.example.onboarding.repository;

import com.example.onboarding.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // Custom query to find a team by its name (optional)
    Team findByName(String name);
}
