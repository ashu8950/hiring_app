package com.example.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
	Optional<Manager> findByUsername(String username);

}