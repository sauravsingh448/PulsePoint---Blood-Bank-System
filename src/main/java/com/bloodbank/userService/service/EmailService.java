package com.bloodbank.userService.service;

public interface EmailService {
    void sendPasswordResetEmail(String toEmail, String token);

    void sendEmail(String to, String subject, String body);
}
