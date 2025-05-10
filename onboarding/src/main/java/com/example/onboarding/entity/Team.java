package com.example.onboarding.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Team name is required")
	private String name;

	// Many-to-one relationship with HR (Each team is managed by one HR)
	@ManyToOne
	private HR hr;

	// One-to-one relationship with Manager (One manager per team)
	@OneToOne(mappedBy = "team")
	private Manager manager;

	// One-to-many relationship with Candidates (Multiple candidates can belong to a
	// team)
	@OneToMany(mappedBy = "team")
	private List<Candidate> candidates;
}
