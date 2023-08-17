package com.epam.service;

import com.epam.ObjectPreparation;
import com.epam.dtos.request.ChangePasswordDto;

import com.epam.dtos.request.TrainingTypeRequestDto;
import com.epam.dtos.response.TrainingTypeResponseDto;
import com.epam.entities.*;
import com.epam.exceptions.UserException;
import com.epam.repositories.*;
import com.epam.services.implementations.LoginServiceImpl;
import com.epam.services.implementations.TrainingTypeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {

    @Mock
    TrainingTypeRepository trainingTypeRepository;

    @InjectMocks
    TrainingTypeServiceImpl trainingTypeService;


    @Test
    void createTrainingType_Success() {
        TrainingTypeRequestDto requestDto = new TrainingTypeRequestDto();
        requestDto.setTrainingTypeName("Fitness");

        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeId(1L);
        trainingType.setTrainingTypeName("Fitness");

        Mockito.when(trainingTypeRepository.save(any()))
                .thenReturn(trainingType);

        TrainingTypeResponseDto result = trainingTypeService.createTrainingType(requestDto);

        assertNotNull(result);
        assertEquals(trainingType.getTrainingTypeId(), result.getTrainingTypeId());
        assertEquals(trainingType.getTrainingTypeName(), result.getTrainingTypeName());

        Mockito.verify(trainingTypeRepository).save(any());
    }


}
