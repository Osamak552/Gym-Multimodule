package com.epam.repositories;

import com.epam.collections.TrainerReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TrainerReportRepository extends MongoRepository<TrainerReport,String> {
    Optional<TrainerReport> findTrainerReportByUsername(String username);
}
