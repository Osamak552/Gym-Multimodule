package com.epam.service;

import com.epam.dtos.request.NotificationDto;
import com.epam.dtos.request.TrainerReportDto;
import com.epam.services.implementations.KafkaNotificationProducerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
public class KafkaNotificationProducerServiceImplTest {
    @Mock
    KafkaTemplate<String, NotificationDto> kafkaTemplate;

    @InjectMocks
    KafkaNotificationProducerServiceImpl kafkaNotificationProducerService;

    @Test
    void sendNotification_Success() {
        NotificationDto notificationDto = NotificationDto.builder().emailInfo(new HashMap<>()).emailType("Test").to("").build();
        String topicName = "notificationTopic";

        Message<NotificationDto> message = MessageBuilder
                .withPayload(notificationDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        Assertions.assertDoesNotThrow(() -> kafkaNotificationProducerService.sendMessage(notificationDto));

    }
}
