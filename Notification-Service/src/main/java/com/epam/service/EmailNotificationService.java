package com.epam.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailNotificationService {
    void sendEmailNotification(SimpleMailMessage simpleMailMessage);
}
