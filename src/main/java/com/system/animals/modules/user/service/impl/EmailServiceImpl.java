package com.system.animals.modules.user.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.system.animals.modules.user.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendCode(String email, String code) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Codigo de verificacion");
        message.setText("Tu codigo de verificacion es: " + code + ". Este codigo vence en 10 minutos.");
        mailSender.send(message);
    }
}
