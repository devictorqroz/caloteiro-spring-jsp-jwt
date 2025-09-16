package com.caloteiros.shared.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String appBaseUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = appBaseUrl + "/password/reset?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(" Sistema Caloteiros | Redefinição de Senha");
        message.setText("Clique no link abaixo para redefinir sua senha:\n" + resetUrl);

        mailSender.send(message);
    }
}