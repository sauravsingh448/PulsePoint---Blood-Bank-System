package com.bloodbank.userService.service.Imp;

import com.bloodbank.userService.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {

        String resetLink = "https://localhost:3000/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Blood Bank Password Reset");
        message.setText(
                "PulsePoint Blood Bank Security Alert\n\n" +
                        "A password reset was requested for your account.\n" +
                        "Use the link below to continue:\n" +
                        resetLink +
                        "\n\nThis link will expire in 15 minutes for security reasons.\n" +
                        "If this wasn't you, please contact support immediately."
        );
        mailSender.send(message);
    }

    // send opt into register email
    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
