package com.epam.services;

import com.epam.dtos.request.NotificationDto;

public interface KafkaNotificationProducerService {
    void sendMessage(NotificationDto notificationDto);
}
