package com.epam.service;

import com.epam.dtos.request.NotificationDto;

public interface KafkaConsumerService {
    void sendMail(NotificationDto notificationDto);
}
