package com.example.onboarding.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class VerifyOtpDTO {

	@NotNull(message = "Phone number is required")
	private String phone;

	@NotNull(message = "OTP is required")
	private String otp;

}
