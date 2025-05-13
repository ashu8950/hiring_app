package com.example.onboarding.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordEmailDTO {

	@NotBlank(message = "Email must not be blank")
	@Email(message = "Invalid email format")
	private String email;
}
