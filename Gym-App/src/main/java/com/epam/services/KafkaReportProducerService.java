package com.epam.services;

import com.epam.dtos.request.TrainerReportDto;

public interface KafkaReportProducerService {
    void sendReport(TrainerReportDto trainerReportDto);
}
