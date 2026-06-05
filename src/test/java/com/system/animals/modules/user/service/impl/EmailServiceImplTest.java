package com.system.animals.modules.user.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.mail.internet.MimeMessage;

class EmailServiceImplTest {

    private TestMailSender mailSender;
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        mailSender = new TestMailSender();
        emailService = new EmailServiceImpl(mailSender);
    }

    @Test
    void sendCodeBuildsVerificationEmailForRecipient() {
        ReflectionTestUtils.setField(emailService, "from", "noreply@test.local");

        emailService.sendCode("person@example.com", "123456");

        SimpleMailMessage message = mailSender.message;
        assertThat(message.getFrom()).isEqualTo("noreply@test.local");
        assertThat(message.getTo()).containsExactly("person@example.com");
        assertThat(message.getSubject()).contains("Codigo de verificacion");
        assertThat(message.getText()).contains("123456");
    }

    private static class TestMailSender implements JavaMailSender {

        private SimpleMailMessage message;

        @Override
        public MimeMessage createMimeMessage() {
            throw new UnsupportedOperationException("MIME messages are not used by this test");
        }

        @Override
        public MimeMessage createMimeMessage(InputStream contentStream) {
            throw new UnsupportedOperationException("MIME messages are not used by this test");
        }

        @Override
        public void send(MimeMessage mimeMessage) {
            throw new UnsupportedOperationException("MIME messages are not used by this test");
        }

        @Override
        public void send(MimeMessage... mimeMessages) {
            throw new UnsupportedOperationException("MIME messages are not used by this test");
        }

        @Override
        public void send(MimeMessagePreparator mimeMessagePreparator) {
            throw new UnsupportedOperationException("MIME messages are not used by this test");
        }

        @Override
        public void send(MimeMessagePreparator... mimeMessagePreparators) {
            throw new UnsupportedOperationException("MIME messages are not used by this test");
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) {
            this.message = simpleMessage;
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) {
            this.message = simpleMessages[0];
        }
    }
}
