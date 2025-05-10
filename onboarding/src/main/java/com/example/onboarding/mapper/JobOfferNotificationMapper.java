package com.example.onboarding.mapper;

import com.example.onboarding.dto.JobOfferNotificationDTO;
import com.example.onboarding.entity.JobOfferNotification;

public class JobOfferNotificationMapper {

	public static JobOfferNotificationDTO toDTO(JobOfferNotification entity) {
		if (entity == null)
			return null;
		return new JobOfferNotificationDTO(entity.getId(), entity.getSent(), entity.getSentAt(), entity.getRetryCount(),
				entity.getErrorMessage(), entity.getCandidate() != null ? entity.getCandidate().getId() : null);
	}

	public static JobOfferNotification toEntity(JobOfferNotificationDTO dto) {
		if (dto == null)
			return null;
		JobOfferNotification entity = new JobOfferNotification();
		entity.setId(dto.getId());
		entity.setSent(dto.getSent());
		entity.setSentAt(dto.getSentAt());
		entity.setRetryCount(dto.getRetryCount());
		entity.setErrorMessage(dto.getErrorMessage());
		// Candidate relation should be handled in service layer
		return entity;
	}
}
