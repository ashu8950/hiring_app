package com.example.onboarding.service.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.onboarding.entity.Candidate;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendJobOfferEmail(String email) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("Job Offer");
		message.setText("Congratulations! You have been selected for the position.");
		mailSender.send(message);
	}
	
	 public void sendJobOfferEmailRabit(Candidate candidate) {
	        try {
	            MimeMessage message = mailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            
	            
	            helper.setTo(candidate.getEmail());
	            log.info(candidate.getEmail());
	            helper.setSubject("🎉 Congratulations " + candidate.getName() + " - Job Offer from Our Company!");

	            String content = generateHtmlContent(candidate);
	            helper.setText(content, true); // true = HTML

	            // Example: adding optional attachment
	            // helper.addAttachment("OfferLetter.pdf", new ClassPathResource("offer-letter.pdf"));

	            mailSender.send(message);

	        } catch (MessagingException e) {
	            throw new RuntimeException("Failed to send email to: " + candidate.getEmail(), e);
	        }
	    }

	    private String generateHtmlContent(Candidate candidate) {
	        return """
	            <html>
	            <body>
	                <h2>Hi %s,</h2>
	                <p>We are thrilled to offer you the position of <strong>%s</strong> at our company!</p>
	                <p>Please check your onboarding portal for further steps.</p>
	                <br>
	                <p>Best regards,<br>The HR Team</p>
	            </body>
	            </html>
	        """.formatted(candidate.getName(), candidate.getRole());
	    }
}
