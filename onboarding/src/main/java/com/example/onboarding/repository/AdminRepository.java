package com.example.onboarding.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.onboarding.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	long count();

	Optional<Admin> findByUsername(String username);

}
