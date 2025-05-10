package com.example.onboarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamDTO {

	private Long id;

	@NotBlank(message = "Team name is required")
	private String name;

	@NotNull(message = "HR ID is required")
	private Long hrId;

	private Long managerId;
}
