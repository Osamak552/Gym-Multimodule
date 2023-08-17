package com.epam.service;

import com.epam.collections.EmailLog;
import com.epam.dtos.request.NotificationDto;
import com.epam.repositories.EmailLogRepository;
import com.epam.service.implementation.EmailIGenerationServiceImpl;
import com.epam.service.implementation.EmailNotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class EmailNotificationServiceImplTest {
    @Mock
    JavaMailSender javaMailSender;

    @Mock
    EmailLogRepository emailLogRepository;

    @InjectMocks
    EmailNotificationServiceImpl emailNotificationService;
    private SimpleMailMessage simpleMailMessage;

    @BeforeEach
    void setUp() {
        simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("recipient@example.com");
        simpleMailMessage.setSubject("Test Subject");
        simpleMailMessage.setText("Test Body");
    }

    @Test
    void testSendEmailNotification() {
        emailNotificationService.sendEmailNotification(simpleMailMessage);

        verify(javaMailSender).send(simpleMailMessage);
        verify(emailLogRepository).save(any(EmailLog.class));
    }
}
