package com.epam.controller;

import com.epam.ObjectPreparation;
import com.epam.controllers.TraineeController;
import com.epam.controllers.TrainingController;
import com.epam.controllers.TrainingTypeController;
import com.epam.dtos.request.*;
import com.epam.dtos.response.*;
import com.epam.services.TraineeService;
import com.epam.services.TrainingService;
import com.epam.services.TrainingTypeService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingTypeController.class)
public class TrainingTypeControllerTest {

    @MockBean
    TrainingTypeService trainingTypeService;

    @Autowired
    MockMvc mockMvc;

    static ObjectMapper objectMapper;

    @BeforeAll
    static void setValues(){
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void createTrainingType() throws Exception {
        TrainingTypeRequestDto trainingTypeRequestDto = TrainingTypeRequestDto.builder().trainingTypeName("Fitness").build();
        TrainingTypeResponseDto trainingTypeResponseDto = TrainingTypeResponseDto.builder()
                .trainingTypeId(1).trainingTypeName("Fitness").build();
        Mockito.when(trainingTypeService.createTrainingType(trainingTypeRequestDto)).thenReturn(trainingTypeResponseDto);

        mockMvc.perform(post("/home/training-type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainingTypeRequestDto)))
                .andExpect(status().isCreated());
        Mockito.verify(trainingTypeService).createTrainingType(trainingTypeRequestDto);
    }
}
