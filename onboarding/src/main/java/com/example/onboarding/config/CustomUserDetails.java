package com.example.onboarding.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.onboarding.entity.Admin;
import com.example.onboarding.entity.Candidate;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;

public class CustomUserDetails implements UserDetails {

	private final Object user; // It will hold any of Admin, HR, Manager, or Candidate

	// Constructor to accept different user types
	public CustomUserDetails(Object user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// Based on the instance of user, determine the role
		if (user instanceof Admin) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (user instanceof HR) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_HR"));
		} else if (user instanceof Manager) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER"));
		} else if (user instanceof Candidate) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_CANDIDATE"));
		}
		return Collections.emptyList();
	}

	@Override
	public String getPassword() {
		if (user instanceof Admin) {
			return ((Admin) user).getPassword();
		} else if (user instanceof HR) {
			return ((HR) user).getPassword();
		} else if (user instanceof Manager) {
			return ((Manager) user).getPassword();
		} else if (user instanceof Candidate) {
			return ((Candidate) user).getPassword();
		}
		return null;
	}

	@Override
	public String getUsername() {
		if (user instanceof Admin) {
			return ((Admin) user).getUsername();
		} else if (user instanceof HR) {
			return ((HR) user).getUsername();
		} else if (user instanceof Manager) {
			return ((Manager) user).getUsername();
		} else if (user instanceof Candidate) {
			return ((Candidate) user).getEmail(); // Candidates use email as username
		}
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	// Getters for role-specific entities
	public Admin getAdmin() {
		if (user instanceof Admin) {
			return (Admin) user;
		}
		return null;
	}

	public HR getHR() {
		if (user instanceof HR) {
			return (HR) user;
		}
		return null;
	}

	public Manager getManager() {
		if (user instanceof Manager) {
			return (Manager) user;
		}
		return null;
	}

	public Candidate getCandidate() {
		if (user instanceof Candidate) {
			return (Candidate) user;
		}
		return null;
	}
}
