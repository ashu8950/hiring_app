package com.example.onboarding.mapper;

import com.example.onboarding.dto.HRDTO;
import com.example.onboarding.entity.HR;

public class HRMapper {

	public static HRDTO toDTO(HR hr) {
		if (hr == null) {
			return null;
		}

		return new HRDTO(
			hr.getId(),
			hr.getUsername(),
			hr.getAdmin() != null ? hr.getAdmin().getId() : null,
			hr.getManager() != null ? hr.getManager().getId() : null,
			hr.getTeam() != null ? hr.getTeam().getId() : null
		);
	}
}
