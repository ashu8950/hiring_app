package com.example.onboarding.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ResetPasswordDTO {

	@NotNull(message = "Phone number is required")
	private String phone;

	@NotNull(message = "OTP is required")
	private String otp;

	@NotNull(message = "New password is required")
	@Size(min = 6, message = "Password must be at least 6 characters long")
	private String newPassword;

}
