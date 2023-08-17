package com.epam.services;

import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.User;


public interface EmailService {

    void registrationEmailInfo(User user);
    void traineeUpdateEmailInfo(Trainee trainee);
    void trainerUpdateEmailInfo(Trainer trainer);
    void trainingEmailInfo(Training training);
}
