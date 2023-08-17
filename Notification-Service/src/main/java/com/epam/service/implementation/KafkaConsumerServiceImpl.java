package com.epam.service.implementation;


import com.epam.dtos.request.NotificationDto;
import com.epam.service.EmailIGenerationService;
import com.epam.service.KafkaConsumerService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    @Autowired
    private EmailIGenerationService emailIGenerationService;
    
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public  void sendMail(NotificationDto notificationDto)
    {
        log.info("Message received from kafka: {}",notificationDto);
        if(notificationDto.getEmailType().equals("Registration")) {
            emailIGenerationService.registrationEmailInfo(notificationDto);
        }
        else if(notificationDto.getEmailType().equals("Trainer_Update")) {
            emailIGenerationService.trainerUpdateEmailInfo(notificationDto);
        }
        else if (notificationDto.getEmailType().equals("Trainee_Update")) {
            emailIGenerationService.traineeUpdateEmailInfo(notificationDto);
        }
        else if (notificationDto.getEmailType().equals("Training_Created")) {
            emailIGenerationService.trainingEmailInfo(notificationDto);
        }
        else
        {
            log.info("Email type not found");
        }
        log.info("Execution ended");
    }

}
