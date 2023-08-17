package com.epam.repositories;

import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training,Long> {
    @Query("SELECT t FROM Training t WHERE (:from IS NULL OR t.trainingDate >= :from) " +
            "AND (:to IS NULL OR t.trainingDate <= :to) " +
            "AND (:trainer IS NULL OR t.trainer = :trainer) " +
            "AND (:trainee IS NULL OR t.trainee = :trainee) "+
            "AND (:trainingType IS NULL OR t.trainingType = :trainingType)")
    List<Training> findAllTrainingInBetween(@Param("from") LocalDate from, @Param("to") LocalDate to,
                                            @Param("trainer") Trainer trainer,
                                            @Param("trainee") Trainee trainee, @Param("trainingType") TrainingType trainingType);

    @Query("SELECT t FROM Training t WHERE (:from IS NULL OR t.trainingDate >= :from) " +
            "AND (:to IS NULL OR t.trainingDate <= :to) " +
            "AND (:trainer IS NULL OR t.trainer = :trainer) " +
            "AND (:trainee IS NULL OR t.trainee = :trainee) ")
    List<Training> findAllTrainingInBetween(@Param("from") LocalDate from, @Param("to") LocalDate to,
                                            @Param("trainer") Trainer trainer,
                                            @Param("trainee") Trainee trainee);
}
