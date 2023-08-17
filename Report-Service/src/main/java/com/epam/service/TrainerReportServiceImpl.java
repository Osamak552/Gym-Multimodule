package com.epam.service;

import com.epam.collections.MonthData;
import com.epam.collections.TrainerReport;
import com.epam.collections.YearData;
import com.epam.repositories.TrainerReportRepository;
import com.epam.dtos.request.TrainerReportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class TrainerReportServiceImpl implements TrainerReportService{

    @Autowired
    private TrainerReportRepository trainerReportRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public void addTrainerReport(TrainerReportDto trainerReportDto){
        log.info("{}::addTrainerReport execution started",this.getClass().getName());
        Optional<TrainerReport> trainerReport = trainerReportRepository.findTrainerReportByUsername(trainerReportDto.getTrainerUsername());
        if(trainerReport.isPresent()) {
            trainerReportExist(trainerReportDto,trainerReport.get());
        }
        else {
            trainerReportNotExist(trainerReportDto);
        }
        log.info("{}::addTrainerReport execution ended",this.getClass().getName());
    }

    private TrainerReport trainerReportNotExist(TrainerReportDto trainerReportDto)
    {
        int trainingDurationSummary = trainerReportDto.getTrainingDuration();
        int year = trainerReportDto.getTrainingDate().getYear();
        Month month = trainerReportDto.getTrainingDate().getMonth();
        TrainerReport newTrainerReport = TrainerReport.builder()
                .username(trainerReportDto.getTrainerUsername())
                .firstName(trainerReportDto.getFirstName())
                .lastName(trainerReportDto.getLastName())
                .isActive(trainerReportDto.isActive())
                .build();
        MonthData monthData = new MonthData(month,trainingDurationSummary);
        YearData yearData = new YearData(year,List.of(monthData));
        newTrainerReport.setYearsList(List.of(yearData));
        newTrainerReport = trainerReportRepository.save(newTrainerReport);
        log.info("New Trainer Report: {} ",newTrainerReport);
        return newTrainerReport;
    }
    private TrainerReport trainerReportExist(TrainerReportDto trainerReportDto,TrainerReport trainerReport) {
        int trainingDurationSummary = trainerReportDto.getTrainingDuration();
        int year = trainerReportDto.getTrainingDate().getYear();
        Month month = trainerReportDto.getTrainingDate().getMonth();
        AtomicBoolean yearIsPresent = new AtomicBoolean(false);

        trainerReport.getYearsList()
                .forEach(yearData -> {

                    if(yearData.getYear() == year)
                    {
                        yearIsPresent.set(true);
                        AtomicBoolean monthIsPresent = new AtomicBoolean(false);
                        yearData.getMonthsList()
                                .forEach(monthData ->
                                {
                                    if(monthData.getMonth() == month)
                                    {
                                        monthData.setTrainingDurationSummary(monthData.getTrainingDurationSummary()+trainingDurationSummary);
                                        monthIsPresent.set(true);
                                    }
                                });
                        if(!monthIsPresent.get())
                        {
                            yearData.getMonthsList().add(new MonthData(month,trainingDurationSummary));
                        }
                    }


                });
        if(!yearIsPresent.get())
        {
            MonthData monthData = new MonthData(month,trainingDurationSummary);
            trainerReport.getYearsList().add(new YearData(year,List.of(monthData)));
        }

        Query query = new Query(Criteria.where("username").is(trainerReportDto.getTrainerUsername()));
        Update update = new Update()
                .set("yearsList", trainerReport.getYearsList())
                .set("firstName", trainerReportDto.getFirstName())
                .set("lastName", trainerReportDto.getLastName())
                .set("isActive", trainerReportDto.isActive());

        mongoTemplate.updateFirst(query, update, TrainerReport.class);
        log.info("Trainer Report: {} ",trainerReport);
        return trainerReport;
    }

}
