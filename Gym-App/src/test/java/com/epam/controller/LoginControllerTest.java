package com.epam.controller;

import com.epam.ObjectPreparation;
import com.epam.controllers.LoginController;
import com.epam.controllers.TraineeController;
import com.epam.dtos.request.*;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.entities.User;
import com.epam.services.LoginService;
import com.epam.services.TraineeService;
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

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @MockBean
    LoginService loginService;

    @Autowired
    MockMvc mockMvc;

    static ObjectMapper objectMapper;


    @BeforeAll
    static void setValues(){
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void validLogin() throws Exception {
        LoginCredential loginCredential = ObjectPreparation.createValidLoginCredential();
        User user = ObjectPreparation.createValidUser();
        Mockito.when(loginService.login(loginCredential.getUsername(),loginCredential.getPassword())).thenReturn(user);
        mockMvc.perform(post("/home/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginCredential)))
                .andExpect(status().isOk());
        Mockito.verify(loginService).login(loginCredential.getUsername(),loginCredential.getPassword());

    }

    @Test
    void invalidLogin() throws Exception {
        LoginCredential loginCredential = ObjectPreparation.createValidLoginCredential();
        User user = ObjectPreparation.createValidUser();
        user.setActive(false);
        Mockito.when(loginService.login(loginCredential.getUsername(),loginCredential.getPassword())).thenReturn(user);
        mockMvc.perform(post("/home/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(loginCredential)))
                .andExpect(status().isOk());
        Mockito.verify(loginService).login(loginCredential.getUsername(),loginCredential.getPassword());
    }

    @Test
    void validChangePassword() throws Exception {
        ChangePasswordDto changePasswordDto = ObjectPreparation.createValidChangePassword();
        Mockito.doNothing().when(loginService).changePassword(changePasswordDto);
        mockMvc.perform(put("/home/login/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk());
        Mockito.verify(loginService).changePassword(changePasswordDto);

    }


}
