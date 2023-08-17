package com.epam.util;


import com.epam.dtos.request.*;
import com.epam.dtos.response.*;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.User;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public  class ObjectConvertor {

    private ObjectConvertor(){};
    public static User getUserFromTraineeDto(TraineeRequestDto traineeRequestDto)
    {
        return User.builder()
                .firstName(traineeRequestDto.getFirstName())
                .lastName(traineeRequestDto.getLastName())
                .email(traineeRequestDto.getEmail())
                .username(traineeRequestDto.getEmail())
                .isActive(true)
                .password("12345")
                .build();
    }

    public static User getUserFromTrainerDto(TrainerRequestDto trainerRequestDto)
    {
        return User.builder()
                .firstName(trainerRequestDto.getFirstName())
                .lastName(trainerRequestDto.getLastName())
                .email(trainerRequestDto.getEmail())
                .username(trainerRequestDto.getEmail())
                .isActive(true)
                .password("12345")
                .build();
    }

    public static TrainerResponseDto trainerToTrainerDto(Trainer trainer)
    {
        return TrainerResponseDto.builder()
                .username(trainer.getUser().getUsername())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName())
                .build();
    }

    public static Trainee traineeDtoToTrainee(TraineeRequestDto traineeRequestDto)
    {
        return Trainee.builder()
                .dateOfBirth(traineeRequestDto.getDateOfBirth())
                .address(traineeRequestDto.getAddress())
                .build();
    }

    public static TraineeResponseDto traineeToTraineeDto(Trainee trainee)
    {
        return TraineeResponseDto.builder()
                .username(trainee.getUser().getUsername())
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .build();
    }

    public static TraineeProfileDto convertToTraineeProfileDto(Trainee trainee,User user)
    {
        List<TrainerResponseDto> trainerResponseDtoList = trainee.getTrainers().stream()
                .map(ObjectConvertor::trainerToTrainerDto).toList();
        return TraineeProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .isActive(user.isActive())
                .dateOfBirth(trainee.getDateOfBirth())
                .address(trainee.getAddress())
                .trainerResponseDtoList(trainerResponseDtoList)
                .build();
    }

    public static TrainerProfileDto convertToTrainerProfileDto(Trainer trainer,User user)
    {
        List<TraineeResponseDto> traineeResponseDtoList = trainer.getTrainees().stream()
                .map(ObjectConvertor::traineeToTraineeDto).toList();
        return TrainerProfileDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .specialization(trainer.getSpecialization().getTrainingTypeName())
                .isActive(user.isActive())
                .traineeResponseDtoList(traineeResponseDtoList)
                .build();
    }

    public static TrainingResponseDto trainingToTrainingResponseDto(Training training)
    {
        return TrainingResponseDto.builder()
                .trainingName(training.getTrainingName())
                .trainingDate(training.getTrainingDate())
                .trainingType(training.getTrainingType().getTrainingTypeName())
                .trainingDuration(training.getTrainingDuration())
                .trainerName(training.getTrainer().getUser().getFirstName()+ " " + training.getTrainer().getUser().getLastName())
                .traineeName(training.getTrainee().getUser().getFirstName()+ " " + training.getTrainee().getUser().getLastName())
                .build();
    }

    public static TrainerReportDto convertToTrainerReportDto(Training training)
    {
        return TrainerReportDto.builder()
                .trainerUsername(training.getTrainer().getUser().getUsername())
                .firstName(training.getTrainer().getUser().getFirstName())
                .lastName(training.getTrainer().getUser().getLastName())
                .isActive(training.getTrainer().getUser().isActive())
                .trainingDate(training.getTrainingDate())
                .trainingDuration(training.getTrainingDuration())
                .build();
    }








}
