package com.epam.controller;


import com.epam.ObjectPreparation;
import com.epam.controllers.TraineeController;
import com.epam.controllers.TrainingController;
import com.epam.dtos.request.*;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.dtos.response.TrainingResponseDto;
import com.epam.exceptions.TrainingException;
import com.epam.services.TraineeService;
import com.epam.services.TrainingService;
import com.epam.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingController.class)
public class TrainingControllerTest {

    @MockBean
    TrainingService trainingService;

    @Autowired
    MockMvc mockMvc;

    static ObjectMapper objectMapper;

    @BeforeAll
    static void setValues(){
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }


    @Test
    void createTraining_Success() throws Exception {
        TrainingRequestDto trainingRequestDto = ObjectPreparation.createValidTrainingRequestDto();
        Mockito.doNothing().when(trainingService).createTraining(trainingRequestDto);
        mockMvc.perform(post("/home/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRequestDto)))
                .andExpect(status().isOk());
        Mockito.verify(trainingService).createTraining(trainingRequestDto);
    }
    @Test
    void createTraining_TrainingException() throws Exception {
        TrainingRequestDto trainingRequestDto = ObjectPreparation.createValidTrainingRequestDto();
        Mockito.doThrow(new TrainingException(Constants.TRAINING_TYPE_NOT_FOUND)).when(trainingService).createTraining(trainingRequestDto);
        mockMvc.perform(post("/home/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingRequestDto)))
                .andExpect(status().isOk());
        Mockito.verify(trainingService).createTraining(trainingRequestDto);
    }



    @Test
    void getTraineeTrainings() throws Exception {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUsername("john.doe@example.com")
                .build();
        List<TrainingResponseDto> trainingResponseDtos = List.of(ObjectPreparation.createTrainingResponseDto());
        Mockito.when(trainingService.getTraineeTrainings(traineeTrainingsDto)).thenReturn(trainingResponseDtos);

        mockMvc.perform(get("/home/training/Trainee-TrainingList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getTrainerTrainings() throws Exception {
        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .build();
        List<TrainingResponseDto> trainingResponseDtos = List.of(ObjectPreparation.createTrainingResponseDto());
        Mockito.when(trainingService.getTrainerTrainings(trainerTrainingsDto)).thenReturn(trainingResponseDtos);

        mockMvc.perform(get("/home/training/Trainer-TrainingList")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
