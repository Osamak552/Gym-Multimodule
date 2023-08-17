package com.epam.service;

import com.epam.dtos.request.TrainerReportDto;
import org.junit.jupiter.api.Test;
import com.epam.collections.TrainerReport;
import com.epam.collections.YearData;
import com.epam.dtos.request.TrainerReportDto;
import com.epam.repositories.TrainerReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

@ExtendWith(MockitoExtension.class)
public class KafkaConsumerServiceImplTest {

    @Mock
    TrainerReportService trainerReportService;
    @InjectMocks
    KafkaConsumerServiceImpl kafkaConsumerService;

    @Test
    void testReportConsumer() {
        TrainerReportDto trainerReportDto = createTrainerReportDto();

        kafkaConsumerService.reportConsumer(trainerReportDto);

        Mockito.verify(trainerReportService, times(1)).addTrainerReport(trainerReportDto);
    }

    private TrainerReportDto createTrainerReportDto() {
        TrainerReportDto dto = new TrainerReportDto();
        dto.setTrainerUsername("john.trainer");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setActive(true);
        return dto;
    }
}
