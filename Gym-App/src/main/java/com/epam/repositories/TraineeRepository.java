package com.epam.repositories;

import com.epam.entities.Trainee;
import com.epam.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraineeRepository extends JpaRepository<Trainee,Long> {

    public Optional<Trainee> findTraineeByUser(User user);
    Optional<Trainee> findByUserUsername(String username);
}
