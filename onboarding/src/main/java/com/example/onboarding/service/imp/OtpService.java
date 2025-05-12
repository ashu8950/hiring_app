package com.example.onboarding.service.imp;

import com.example.onboarding.entity.OtpToken;
import com.example.onboarding.repository.OtpTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpTokenRepository otpRepo;

    public String generateOtp(String phone) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        OtpToken token = new OtpToken(null, phone, otp, LocalDateTime.now().plusMinutes(5));
        otpRepo.save(token);
        return otp;
    }

    public boolean verifyOtp(String phone, String otp) {
        Optional<OtpToken> token = otpRepo.findByPhoneAndOtp(phone, otp);
        return token.isPresent() && token.get().getExpiresAt().isAfter(LocalDateTime.now());
    }
}
