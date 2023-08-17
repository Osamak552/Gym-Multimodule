package com.epam.services;

import com.epam.dtos.request.TraineeTrainingsDto;
import com.epam.dtos.request.TrainerTrainingsDto;
import com.epam.dtos.request.TrainingRequestDto;
import com.epam.dtos.response.TrainingResponseDto;


import java.util.List;

public interface TrainingService {
    void createTraining(TrainingRequestDto trainingRequestDto);
    List<TrainingResponseDto> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto);
    List<TrainingResponseDto> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto);
}
