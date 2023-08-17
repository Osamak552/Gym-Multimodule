package com.epam.repositories;

import com.epam.entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType,Long> {
    public Optional<TrainingType> findTrainingTypeByTrainingTypeName(String trainingTypeName);
}
