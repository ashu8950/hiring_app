package com.example.onboarding.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.example.onboarding.dto.AdminDTO;
import com.example.onboarding.entity.Admin;
import com.example.onboarding.entity.HR;
import com.example.onboarding.entity.Manager;

public class AdminMapper {

	// Convert Admin entity to AdminDTO
	public static AdminDTO toDTO(Admin admin) {
		if (admin == null) {
			return null;
		}

		List<Long> managerIds = admin.getManagers().stream().map(Manager::getId).collect(Collectors.toList());

		List<Long> hrIds = admin.getHrs().stream().map(HR::getId).collect(Collectors.toList());

		return new AdminDTO(admin.getId(), admin.getUsername(), managerIds, hrIds, admin.getCreatedAt(),
				admin.getUpdatedAt());
	}

	// Convert AdminDTO to Admin entity
	public static Admin toEntity(AdminDTO dto) {
		if (dto == null) {
			return null;
		}

		Admin admin = new Admin();
		admin.setId(dto.getId());
		admin.setUsername(dto.getUsername());
		admin.setCreatedAt(dto.getCreatedAt());
		admin.setUpdatedAt(dto.getUpdatedAt());

		// Manager and HR handling can be done in the service layer
		// For now, we can set these as placeholders
		return admin;
	}
}
