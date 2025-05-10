package com.example.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BranchDTO {
	private Long id;

	@NotBlank(message = "Branch name is required")
	private String name;
}
