package com.epam.controller;


import com.epam.ObjectPreparation;
import com.epam.controllers.LoginController;
import com.epam.controllers.TraineeController;
import com.epam.controllers.TrainerController;
import com.epam.dtos.request.*;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.entities.User;
import com.epam.exceptions.TrainerException;
import com.epam.exceptions.TrainingTypeException;
import com.epam.exceptions.UserException;
import com.epam.services.LoginService;
import com.epam.services.TraineeService;
import com.epam.services.TrainerService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainerController.class)
public class TrainerControllerTest {

    @MockBean
    TrainerService trainerService;

    @Autowired
    MockMvc mockMvc;
    static ObjectMapper objectMapper;

    @BeforeAll
    static void setValues(){
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void createTrainer_Success() throws Exception {
        TrainerRequestDto trainerRequestDto = ObjectPreparation.createValidTrainerRequestDto();
        TrainerResponseDto trainerResponseDto = ObjectPreparation.createTrainerResponseDto();
        Mockito.when(trainerService.createTrainer(trainerRequestDto)).thenReturn(trainerResponseDto);

        mockMvc.perform(post("/home/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isCreated());
        Mockito.verify(trainerService).createTrainer(trainerRequestDto);
    }

    @Test
    void createTrainer_InvalidRequestBody_BadRequest() throws Exception {
        TrainerRequestDto trainerRequestDto = new TrainerRequestDto(); // Invalid request body

        mockMvc.perform(post("/home/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(trainerService);
    }

    @Test
    void createTrainer_InvalidRequestBody_EmptyFirstName() throws Exception {
        TrainerRequestDto trainerRequestDto = ObjectPreparation.createValidTrainerRequestDto(); // Invalid request body
        trainerRequestDto.setFirstName(null);
        mockMvc.perform(post("/home/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isBadRequest());
        Mockito.verifyNoInteractions(trainerService);
    }

    @Test
    void createTrainer_SpecializationNotFound() throws Exception {
        TrainerRequestDto trainerRequestDto = ObjectPreparation.createValidTrainerRequestDto();
        Mockito.when(trainerService.createTrainer(trainerRequestDto))
                .thenThrow(new TrainingTypeException(Constants.TRAINING_TYPE_NOT_FOUND));

        mockMvc.perform(post("/home/trainer/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerRequestDto)))
                .andExpect(status().isOk());
        Mockito.verify(trainerService).createTrainer(trainerRequestDto);
    }


    @Test
    void getTrainer_Success() throws Exception {
        String username = "john.trainer.doe@example.com";
        TrainerProfileDto trainerProfileDto = ObjectPreparation.createValidTrainerProfileDto();
        Mockito.when(trainerService.getTrainerProfile(username)).thenReturn(trainerProfileDto);

        mockMvc.perform(get("/home/trainer/{username}",username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(trainerService).getTrainerProfile(username);
    }

    @Test
    void getTrainer_InvalidUsername_NotFound() throws Exception {
        String username = "nonexistent.trainer@example.com";
        Mockito.when(trainerService.getTrainerProfile(username))
                .thenThrow(new UserException(Constants.USER_NOT_FOUND));

        mockMvc.perform(get("/home/trainer/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(trainerService).getTrainerProfile(username);
    }

    @Test
    void getTrainer_TrainerNotFound() throws Exception {
        String username = "nonexistent.trainer@example.com";
        Mockito.when(trainerService.getTrainerProfile(username))
                .thenThrow(new TrainerException(Constants.TRAINER_NOT_FOUND));

        mockMvc.perform(get("/home/trainer/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(trainerService).getTrainerProfile(username);
    }
    @Test
    void updateTrainer() throws Exception {
        String username = "john.doe@example.com";
        TrainerUpdateRequestDto trainerUpdateRequestDto = ObjectPreparation.createValidTrainerUpdateRequestDto();
        TrainerProfileDto trainerProfileDto = ObjectPreparation.createValidTrainerProfileDto();
        Mockito.when(trainerService.updateTrainerProfile(username,trainerUpdateRequestDto)).thenReturn(trainerProfileDto);

        mockMvc.perform(put("/home/trainer/{username}",username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(trainerProfileDto)))
                .andExpect(status().isOk());
        Mockito.verify(trainerService).updateTrainerProfile(username,trainerUpdateRequestDto);
    }

    @Test
    void getUnassignedTrainerFromTrainee() throws Exception {
        String username = "john.doe@example.com";
        List<TrainerResponseDto> trainerResponseDtoList = List.of(ObjectPreparation.createValidTrainerResponseDto());
        Mockito.when(trainerService.getUnassignedTrainersFromTrainee(username)).thenReturn(trainerResponseDtoList);

        mockMvc.perform(get("/home/trainer/unassigned-trainer/{traineeUsername}",username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(trainerService).getUnassignedTrainersFromTrainee(username);
    }

}
