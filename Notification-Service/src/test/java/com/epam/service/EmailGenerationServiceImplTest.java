package com.epam.service;


import com.epam.dtos.request.NotificationDto;
import com.epam.service.implementation.EmailIGenerationServiceImpl;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailGenerationServiceImplTest {
    @Mock
    EmailNotificationService emailNotificationService;

    @InjectMocks
    EmailIGenerationServiceImpl emailIGenerationService;

    private NotificationDto notificationDto;

    @BeforeEach
    void setUp() {
        notificationDto = new NotificationDto();
        Map<String, String> emailInfo = new HashMap<>();
        emailInfo.put("username", "testUser");
        emailInfo.put("password", "testPassword");
        emailInfo.put("firstName", "John");
        emailInfo.put("lastName", "Doe");
        emailInfo.put("dateOfBirth", "2000-01-01");
        emailInfo.put("address", "123 Main St");
        emailInfo.put("trainerUsername", "trainer123");
        emailInfo.put("trainingName", "Java Programming");
        emailInfo.put("traineeUsername", "trainee456");
        emailInfo.put("trainingType", "Online");
        emailInfo.put("date", "2023-08-01");
        emailInfo.put("duration", "60 minutes");
        emailInfo.put("specialization", "Software Development");
        notificationDto.setEmailInfo(emailInfo);
    }

    @Test
    void testRegistrationEmailInfo() {
        emailIGenerationService.registrationEmailInfo(notificationDto);

        verify(emailNotificationService).sendEmailNotification(any(SimpleMailMessage.class));
    }

    @Test
    void testTraineeUpdateEmailInfo() {
        emailIGenerationService.traineeUpdateEmailInfo(notificationDto);

        verify(emailNotificationService).sendEmailNotification(any(SimpleMailMessage.class));
    }

    @Test
    void testTrainerUpdateEmailInfo() {
        emailIGenerationService.trainerUpdateEmailInfo(notificationDto);

        verify(emailNotificationService).sendEmailNotification(any(SimpleMailMessage.class));
    }

    @Test
    void testTrainingEmailInfo() {
        emailIGenerationService.trainingEmailInfo(notificationDto);

        verify(emailNotificationService).sendEmailNotification(any(SimpleMailMessage.class));
    }

}
