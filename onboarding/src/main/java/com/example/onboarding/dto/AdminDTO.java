package com.example.onboarding.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {

	private Long id;
	private String username;
	private List<Long> managerIds; // List of manager IDs
	private List<Long> hrIds; // List of HR IDs
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
