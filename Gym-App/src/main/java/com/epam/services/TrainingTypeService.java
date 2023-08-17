package com.epam.services;

import com.epam.dtos.request.TrainingTypeRequestDto;
import com.epam.dtos.response.TrainingTypeResponseDto;

public interface TrainingTypeService {
    TrainingTypeResponseDto createTrainingType(TrainingTypeRequestDto trainingTypeRequestDto);
}
