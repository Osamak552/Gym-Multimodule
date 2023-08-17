package com.epam.service;

import com.epam.dtos.request.TrainerReportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService{

    @Autowired
    private TrainerReportService trainerReportService;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void reportConsumer(TrainerReportDto trainerReportDto)
    {
        trainerReportService.addTrainerReport(trainerReportDto);
        log.info("Message received from kafka: {}",trainerReportDto);

    }
}
