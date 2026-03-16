package com.kwyjibo.VaultGuard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("{spring.mail.username}")
    private String senderEmail;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendCredentialsEmail(String toEmail, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vaultguard@kwyjibo.com");
        message.setTo(toEmail);
        message.setSubject("JIT Access Approved - VaultGuard");
        message.setText("Your Just-In-Time access has been approved.\n\n" +
                        "Username: " + username + "\n" +
                        "Password: " + password + "\n\n" +
                        "This access will expire in 1 hour.");
        
        mailSender.send(message);
    }
}
