package com.epam.services;

import com.epam.dtos.request.AssignTrainersRequestDto;
import com.epam.dtos.request.TraineeRequestDto;
import com.epam.dtos.request.TraineeUpdateRequestDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;

import java.util.List;

public interface TraineeService {
    TraineeResponseDto createTrainee(TraineeRequestDto traineeRequestDto);
    TraineeProfileDto getTraineeProfile(String username);
    TraineeProfileDto updateTraineeProfile(String username, TraineeUpdateRequestDto traineeUpdateRequestDto);
    List<TrainerResponseDto> assignTrainers(AssignTrainersRequestDto assignTrainersRequestDto);
    void deleteTrainee(String username);

}
