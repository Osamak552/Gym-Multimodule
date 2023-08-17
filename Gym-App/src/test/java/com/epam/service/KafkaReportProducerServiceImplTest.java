package com.epam.service;

import com.epam.dtos.request.TrainerReportDto;
import com.epam.services.implementations.KafkaReportProducerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import static org.bouncycastle.math.ec.rfc8032.Ed25519.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaReportProducerServiceImplTest {
    @Mock
    KafkaTemplate<String, TrainerReportDto> kafkaTemplate;

    @InjectMocks
    KafkaReportProducerServiceImpl kafkaReportProducerService;



    @Test
    void sendReport_Success() {
        TrainerReportDto trainerReportDto = TrainerReportDto.builder().trainerUsername("demo@gmail.com").build();
        String topicName = "reportTopic";

        Message<TrainerReportDto> message = MessageBuilder
                .withPayload(trainerReportDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();

        Assertions.assertDoesNotThrow(() -> kafkaReportProducerService.sendReport(trainerReportDto));

    }
}
