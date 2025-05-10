package com.example.onboarding.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.onboarding.config.RabbitMQConfig;
import com.example.onboarding.repository.CandidateRepository;
import com.example.onboarding.service.imp.EmailService;

@Component
public class JobOfferListener {

	private final CandidateRepository candidateRepository;
	private final EmailService emailService;

	@Autowired
	public JobOfferListener(CandidateRepository candidateRepository, EmailService emailService) {
		this.candidateRepository = candidateRepository;
		this.emailService = emailService;
	}

	@RabbitListener(queues = RabbitMQConfig.QUEUE)
	public void handleJobOffer(Long candidateId) {
		candidateRepository.findById(candidateId)
				.ifPresentOrElse(candidate -> emailService.sendJobOfferEmailRabit(candidate), () -> {
					throw new RuntimeException("Candidate not found with ID: " + candidateId);
				});
	}
}
