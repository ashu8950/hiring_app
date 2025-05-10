package com.example.onboarding.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferNotificationDTO {
	private Long id;
	private Boolean sent;
	private LocalDateTime sentAt;
	private Integer retryCount;
	private String errorMessage;
	private Long candidateId;
}