package com.epam.service;

import com.epam.dtos.request.NotificationDto;

public interface EmailIGenerationService {
    void registrationEmailInfo(NotificationDto notificationDto);
    void traineeUpdateEmailInfo(NotificationDto notificationDto);
    void trainerUpdateEmailInfo(NotificationDto notificationDto);
    void trainingEmailInfo(NotificationDto notificationDto);
}
