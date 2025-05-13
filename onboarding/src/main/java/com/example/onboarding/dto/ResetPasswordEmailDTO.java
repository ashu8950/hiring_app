package com.example.onboarding.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordEmailDTO {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "OTP must not be blank")
    private String otp;

    @NotBlank(message = "New password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String newPassword;
}
