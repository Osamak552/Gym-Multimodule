package com.epam.services.implementations;

import com.epam.repositories.TrainingTypeRepository;
import com.epam.dtos.request.TrainingTypeRequestDto;
import com.epam.dtos.response.TrainingTypeResponseDto;
import com.epam.entities.TrainingType;
import com.epam.services.TrainingTypeService;
import com.epam.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingTypeServiceImpl implements TrainingTypeService {

    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    public TrainingTypeResponseDto createTrainingType(TrainingTypeRequestDto trainingTypeRequestDto)
    {
        log.info("{}:: createTrainingType {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TrainingType trainingType = TrainingType.builder()
                .trainingTypeName(trainingTypeRequestDto.getTrainingTypeName())
                .build();
        trainingType = trainingTypeRepository.save(trainingType);
        log.info("{}:: createTrainingType {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return TrainingTypeResponseDto.builder()
                .trainingTypeId(trainingType.getTrainingTypeId())
                .trainingTypeName(trainingType.getTrainingTypeName())
                .build();
    }
}
