package com.epam.service;

import com.epam.collections.EmailLog;
import com.epam.dtos.request.NotificationDto;
import com.epam.repositories.EmailLogRepository;
import com.epam.service.implementation.EmailIGenerationServiceImpl;
import com.epam.service.implementation.EmailNotificationServiceImpl;
import com.epam.service.implementation.KafkaConsumerServiceImpl;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;


@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceImplTest {
    @Mock
    private EmailIGenerationService emailIGenerationService;

    @InjectMocks
    private KafkaConsumerServiceImpl kafkaConsumerService;

    private NotificationDto notificationDto;

    @BeforeEach
    void setUp() {
        notificationDto = new NotificationDto();
        notificationDto.setTo("recipient@example.com");

        // Set other fields as needed
    }

    @Test
    void testSendMailRegistration() {
        notificationDto.setEmailType("Registration");
        kafkaConsumerService.sendMail(notificationDto);

        verify(emailIGenerationService).registrationEmailInfo(notificationDto);
    }

    @Test
    void testSendMailTrainerUpdate() {
        notificationDto.setEmailType("Trainer_Update");
        kafkaConsumerService.sendMail(notificationDto);

        verify(emailIGenerationService).trainerUpdateEmailInfo(notificationDto);
    }

    @Test
    void testSendMailTraineeUpdate() {
        notificationDto.setEmailType("Trainee_Update");
        kafkaConsumerService.sendMail(notificationDto);

        verify(emailIGenerationService).traineeUpdateEmailInfo(notificationDto);
    }

    @Test
    void testSendMailTrainingCreated() {
        notificationDto.setEmailType("Training_Created");
        kafkaConsumerService.sendMail(notificationDto);

        verify(emailIGenerationService).trainingEmailInfo(notificationDto);
    }

    @Test
    void testSendMailInvalidType() {
        notificationDto.setEmailType("Invalid_Type");
        kafkaConsumerService.sendMail(notificationDto);

        verifyNoInteractions(emailIGenerationService);
    }
}
