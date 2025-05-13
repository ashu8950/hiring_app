package com.example.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.example.onboarding.config.JwtProperties;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
@EnableCaching
public class OnboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnboardingApplication.class, args);
		log.info("server is running");
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		String rawPassword = "john1.doe";
//		String hashedPasswordFromDb = "$2a$10$YWN7waQRHN9Fri23Xzb3teJdvWSca1LfiRlCkYEP4Wt.Uz4ga3.ii"; // paste here
//
//		boolean matches = encoder.matches(rawPassword, hashedPasswordFromDb);
//		System.out.println("Matches? " + matches);
	}

}
