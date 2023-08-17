package com.epam.services.implementations;

import com.epam.dtos.request.TrainerReportDto;
import com.epam.services.KafkaReportProducerService;
import com.epam.util.Constants;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaReportProducerServiceImpl implements KafkaReportProducerService {
    @Autowired
    private KafkaTemplate<String, TrainerReportDto> kafkaTemplate;
    @Value("${spring.kafka.topic.report}")
    private String topicName;
    public void sendReport(TrainerReportDto trainerReportDto)
    {
        log.info("{}::sendMessage {}",this.getClass().getName() , Constants.EXECUTION_STARTED);
        Message<TrainerReportDto> message = MessageBuilder
                .withPayload(trainerReportDto)
                .setHeader(KafkaHeaders.TOPIC, topicName)
                .build();
            kafkaTemplate.send(message);
        log.info("{}::sendMessage {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
    }

    @PreDestroy
    public void close(){
        if(kafkaTemplate != null)
        {
            log.info("{}:: closing kafkaTemplate",this.getClass().getName());
            kafkaTemplate.destroy();
        }
    }
}
