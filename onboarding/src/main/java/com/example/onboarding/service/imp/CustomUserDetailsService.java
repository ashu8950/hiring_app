package com.example.onboarding.service.imp;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.onboarding.entity.Admin;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;
import com.example.onboarding.repository.AdminRepository;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.repository.HRRepository;
import com.example.onboarding.repository.ManagerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;
	private final HRRepository hrRepository;
	private final ManagerRepository managerRepository;
	private final CandidateRepository candidateRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Try to find the user in each role repository
		Admin admin = adminRepository.findByUsername(username).orElse(null);
		if (admin != null) {
			return new User(admin.getUsername(), admin.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
		}

		HR hr = hrRepository.findByUsername(username).orElse(null);
		if (hr != null) {
			return new User(hr.getUsername(), hr.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("HR")));
		}

		Manager manager = managerRepository.findByUsername(username).orElse(null);
		if (manager != null) {
			return new User(manager.getUsername(), manager.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("MANAGER")));
		}

		Candidate candidate = candidateRepository.findByName(username).orElse(null);
		if (candidate != null) {
			return new User(candidate.getEmail(), candidate.getPassword(),
					Collections.singletonList(new SimpleGrantedAuthority("CANDIDATE")));
		}

		throw new UsernameNotFoundException("User not found with username or email: " + username);
	}
}
