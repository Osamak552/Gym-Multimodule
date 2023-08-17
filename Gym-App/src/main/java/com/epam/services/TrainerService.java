package com.epam.services;

import com.epam.dtos.request.TrainerRequestDto;
import com.epam.dtos.request.TrainerUpdateRequestDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerResponseDto;

import java.util.List;

public interface TrainerService {
    TrainerResponseDto createTrainer(TrainerRequestDto trainerRequestDto);
    TrainerProfileDto getTrainerProfile(String username);
    TrainerProfileDto updateTrainerProfile(String username, TrainerUpdateRequestDto trainerUpdateRequestDto);
    List<TrainerResponseDto> getUnassignedTrainersFromTrainee(String traineeUsername);
}
