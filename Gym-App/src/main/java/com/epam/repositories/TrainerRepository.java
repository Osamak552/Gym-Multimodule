package com.epam.repositories;

import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    Optional<Trainer> findTrainerByUser(User user);
    Optional<Trainer> findByUserUsername(String username);
    @Query("SELECT t FROM Trainer t WHERE t.user.isActive = true AND :trainee NOT MEMBER OF t.trainees")
    List<Trainer> findActiveTrainersNotAssignedToTrainee(@Param("trainee") Trainee trainee);
}
