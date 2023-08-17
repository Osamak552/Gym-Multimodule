package com.epam.controller;


import com.epam.ObjectPreparation;
import com.epam.controllers.TraineeController;
import com.epam.dtos.request.AssignTrainersRequestDto;
import com.epam.dtos.request.TraineeRequestDto;
import com.epam.dtos.request.TraineeUpdateRequestDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.exceptions.TraineeException;
import com.epam.services.TraineeService;
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



@WebMvcTest(TraineeController.class)
public class TraineeControllerTest {

    @MockBean
    TraineeService traineeService;

    @Autowired
    MockMvc mockMvc;


    static ObjectMapper objectMapper;


    @BeforeAll
    static void setValues(){
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void creatTrainee_Success() throws Exception {
        TraineeRequestDto traineeRequestDto = ObjectPreparation.createValidTraineeRequestDto();
        TraineeResponseDto traineeResponseDto = ObjectPreparation.createValidTraineeResponseDto();
        Mockito.when(traineeService.createTrainee(traineeRequestDto)).thenReturn(traineeResponseDto);

        mockMvc.perform(post("/home/trainee/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeRequestDto)))
                .andExpect(status().isCreated());
        Mockito.verify(traineeService).createTrainee(traineeRequestDto);
    }
    @Test
    void createTrainee_InvalidRequest_NullDto() throws Exception {
        TraineeRequestDto invalidRequestDto = new TraineeRequestDto(); // Incomplete DTO
        mockMvc.perform(post("/home/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(traineeService, Mockito.never()).createTrainee(Mockito.any());
    }

    @Test
    void createTrainee_InvalidRequest_EmptyFirstName() throws Exception {
        TraineeRequestDto invalidRequestDto = ObjectPreparation.createValidTraineeRequestDto(); // Incomplete DTO firstname = null
        invalidRequestDto.setFirstName(null);
        mockMvc.perform(post("/home/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(traineeService, Mockito.never()).createTrainee(Mockito.any());
    }

    @Test
    void createTrainee_InvalidRequest_EmptyLastName() throws Exception {
        TraineeRequestDto invalidRequestDto = ObjectPreparation.createValidTraineeRequestDto(); // Incomplete DTO lastname = null
        invalidRequestDto.setLastName(null);
        mockMvc.perform(post("/home/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(traineeService, Mockito.never()).createTrainee(Mockito.any());
    }

    @Test
    void createTrainee_InvalidRequest_InvalidEmailFormat() throws Exception {
        TraineeRequestDto invalidRequestDto = ObjectPreparation.createValidTraineeRequestDto(); // Incomplete DTO firstname = null
        invalidRequestDto.setEmail("wrongFormatEmail");
        mockMvc.perform(post("/home/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        Mockito.verify(traineeService, Mockito.never()).createTrainee(Mockito.any());
    }

    @Test
    void createTrainee_ServerError() throws Exception {
        TraineeRequestDto traineeRequestDto = ObjectPreparation.createValidTraineeRequestDto();
        Mockito.when(traineeService.createTrainee(traineeRequestDto)).thenThrow(new RuntimeException());

        mockMvc.perform(post("/home/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequestDto)))
                .andExpect(status().isInternalServerError());

        Mockito.verify(traineeService).createTrainee(traineeRequestDto);
    }

    @Test
    void createTrainee_UsernameConflict() throws Exception {
        TraineeRequestDto traineeRequestDto = ObjectPreparation.createValidTraineeRequestDto();
        Mockito.when(traineeService.createTrainee(traineeRequestDto)).thenThrow(new TraineeException("Username already exists"));

        mockMvc.perform(post("/home/trainee/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeRequestDto)))
                .andExpect(status().isOk());

        Mockito.verify(traineeService).createTrainee(traineeRequestDto);
    }
    @Test
    void getTrainee() throws Exception {
        String username = "john.doe@example.com";
        TraineeProfileDto traineeProfileDto = ObjectPreparation.createValidTraineeProfileDto();
        Mockito.when(traineeService.getTraineeProfile(username)).thenReturn(traineeProfileDto);

        mockMvc.perform(get("/home/trainee/{username}",username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).getTraineeProfile(username);
    }

    @Test
    void getTrainee_ProfileNotFound_ReturnsNotFound() throws Exception {
        String invalidUsername = "invalid.username@example.com";
        Mockito.when(traineeService.getTraineeProfile(invalidUsername))
                .thenThrow(new TraineeException(Constants.TRAINEE_NOT_FOUND));

        mockMvc.perform(get("/home/trainee/{username}", invalidUsername)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).getTraineeProfile(invalidUsername);
    }

    @Test
    void getTrainee_ServerError_ReturnsInternalServerError() throws Exception {
        String username = "john.doe@example.com";
        Mockito.when(traineeService.getTraineeProfile(username)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/home/trainee/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        Mockito.verify(traineeService).getTraineeProfile(username);
    }

    @Test
    void updateTrainee() throws Exception {
        String username = "john.doe@example.com";
        TraineeUpdateRequestDto traineeUpdateRequestDto = ObjectPreparation.createValidTraineeUpdateRequestDto();
        TraineeProfileDto traineeProfileDto = ObjectPreparation.createValidTraineeProfileDto();
        Mockito.when(traineeService.updateTraineeProfile(username,traineeUpdateRequestDto)).thenReturn(traineeProfileDto);

        mockMvc.perform(put("/home/trainee/{username}",username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeProfileDto)))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).updateTraineeProfile(username,traineeUpdateRequestDto);
    }

    @Test
    void updateTrainee_ProfileNotFound_ReturnsNotFound() throws Exception {
        String invalidUsername = "invalid.username@example.com";
        TraineeUpdateRequestDto traineeUpdateRequestDto = ObjectPreparation.createValidTraineeUpdateRequestDto();
        Mockito.when(traineeService.updateTraineeProfile(invalidUsername, traineeUpdateRequestDto))
                .thenThrow(new TraineeException(Constants.TRAINEE_NOT_FOUND));
        mockMvc.perform(put("/home/trainee/{username}", invalidUsername)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeUpdateRequestDto)))
                .andExpect(status().isOk());

        Mockito.verify(traineeService).updateTraineeProfile(invalidUsername, traineeUpdateRequestDto);
    }

    @Test
    void updateTrainee_ServerError_ReturnsInternalServerError() throws Exception {
        String username = "john.doe@example.com";
        TraineeUpdateRequestDto traineeUpdateRequestDto = ObjectPreparation.createValidTraineeUpdateRequestDto();
        Mockito.when(traineeService.updateTraineeProfile(username, traineeUpdateRequestDto))
                .thenThrow(new RuntimeException());

        mockMvc.perform(put("/home/trainee/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeUpdateRequestDto)))
                .andExpect(status().isInternalServerError());

        Mockito.verify(traineeService).updateTraineeProfile(username, traineeUpdateRequestDto);
    }

    @Test
    void assignTrainersToTrainee() throws Exception {
        String username = "john.doe@example.com";
        AssignTrainersRequestDto assignTrainersRequestDto = ObjectPreparation.createValidAssignTrainersRequestDto();
        List<TrainerResponseDto> trainerResponseDtoList = List.of(ObjectPreparation.createValidTrainerResponseDto());

        Mockito.when(traineeService.assignTrainers(assignTrainersRequestDto)).thenReturn(trainerResponseDtoList);

        mockMvc.perform(put("/home/trainee/assign-trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignTrainersRequestDto)))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).assignTrainers(assignTrainersRequestDto);
    }

    @Test
    void deleteTrainee() throws Exception {
        String username = "john.doe@example.com";

        Mockito.doNothing().when(traineeService).deleteTrainee(username);

        mockMvc.perform(delete("/home/trainee/{username}",username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(traineeService).deleteTrainee(username);
    }





}
