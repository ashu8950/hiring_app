package com.example.onboarding.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.onboarding.config.RabbitMQConfig;

@Component
public class JobOfferProducer {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendJobOffer(Long candidateId) {
		// Sending the message (candidateId) to the specified RabbitMQ queue
		rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, candidateId);
	}
}
