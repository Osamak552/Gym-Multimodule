package com.epam.service;

import com.epam.collections.TrainerReport;
import com.epam.collections.YearData;
import com.epam.dtos.request.TrainerReportDto;
import com.epam.repositories.TrainerReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrainerReportServiceImplTest {
    @Mock
    private TrainerReportRepository trainerReportRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private TrainerReportServiceImpl trainerReportService;

    @Test
    void testAddTrainerReportNewReport() {
        TrainerReportDto trainerReportDto = createTrainerReportDto();
        Mockito.when(trainerReportRepository.findTrainerReportByUsername(trainerReportDto.getTrainerUsername())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> trainerReportService.addTrainerReport(trainerReportDto));
        Mockito.verify(trainerReportRepository, times(1)).save(any(TrainerReport.class));
    }

    @Test
    void testAddTrainerReportExistingReport() {
        TrainerReportDto trainerReportDto = createTrainerReportDto();
        TrainerReport existingReport = createExistingTrainerReport();
        when(trainerReportRepository.findTrainerReportByUsername(trainerReportDto.getTrainerUsername())).thenReturn(Optional.of(existingReport));
        assertDoesNotThrow(() -> trainerReportService.addTrainerReport(trainerReportDto));

        Mockito.verify(trainerReportRepository).findTrainerReportByUsername(trainerReportDto.getTrainerUsername());
    }




    private TrainerReportDto createTrainerReportDto() {
        TrainerReportDto dto = new TrainerReportDto();
        dto.setTrainerUsername("john.trainer");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setActive(true);
        dto.setTrainingDate(LocalDate.of(2023, Month.AUGUST, 15));
        dto.setTrainingDuration(60);
        return dto;
    }

    private TrainerReport createExistingTrainerReport() {
        TrainerReport report = TrainerReport.builder().build();
        report.setUsername("john.trainer");
        report.setFirstName("John");
        report.setLastName("Doe");
        report.setActive(true);
        YearData yearData = new YearData(2023, new ArrayList<>());
        report.setYearsList(List.of(yearData));
        return report;
    }


}
