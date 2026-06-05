package com.system.animals.modules.user.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.system.animals.modules.user.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String from;

    @Override
    public void sendCode(String email, String code) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);
        message.setTo(email);
        message.setSubject("Codigo de verificacion - System Animals");
        message.setText("""
                Hola,

                Tu codigo de verificacion es: %s

                Este codigo vence en 10 minutos. Si no solicitaste este correo, puedes ignorarlo.

                System Animals
                """.formatted(code));
        mailSender.send(message);
    }
}
