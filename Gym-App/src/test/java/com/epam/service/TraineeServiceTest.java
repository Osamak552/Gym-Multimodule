package com.epam.service;

import com.epam.ObjectPreparation;
import com.epam.dtos.request.AssignTrainersRequestDto;
import com.epam.dtos.request.NotificationDto;
import com.epam.dtos.request.TraineeRequestDto;
import com.epam.dtos.request.TraineeUpdateRequestDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.User;
import com.epam.exceptions.TraineeException;
import com.epam.exceptions.TrainerException;
import com.epam.exceptions.UserException;
import com.epam.repositories.TraineeRepository;
import com.epam.repositories.TrainerRepository;
import com.epam.repositories.UserRepository;
import com.epam.services.KafkaNotificationProducerService;
import com.epam.services.implementations.TraineeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TraineeServiceTest {
    @Mock
    TraineeRepository traineeRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserRepository userRepository;
    @Mock
    KafkaNotificationProducerService kafkaNotificationProducerService;
    @InjectMocks
    TraineeServiceImpl traineeService;


    @Test
    void createTrainee() {
        TraineeRequestDto traineeRequestDto = ObjectPreparation.createValidTraineeRequestDto();
        User user = ObjectPreparation.createValidUser();
        NotificationDto notificationDto = NotificationDto.builder().emailInfo(new HashMap<String, String>() {{
                    put("password", "12345");
                    put("username", "john.doe@example.com");
                }}).emailType("Registration")
                .to(user.getUsername()).build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        TraineeResponseDto traineeResponseDto = ObjectPreparation.createValidTraineeResponseDto();

        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.doNothing().when(kafkaNotificationProducerService).sendMessage(notificationDto);

        TraineeResponseDto result = traineeService.createTrainee(traineeRequestDto);
        assertEquals(traineeResponseDto, result);
        Mockito.verify(traineeRepository).save(trainee);
        Mockito.verify(kafkaNotificationProducerService).sendMessage(notificationDto);

    }
    @Test
    void getTraineeProfile(){
        String username = "john.doe@example.com";
        User user = ObjectPreparation.createValidUser();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        TraineeProfileDto traineeProfileDto = ObjectPreparation.createValidTraineeProfileDto();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(traineeRepository.findTraineeByUser(user)).thenReturn(Optional.of(trainee));
        TraineeProfileDto result = traineeService.getTraineeProfile(username);
        assertEquals(traineeProfileDto,result);
        Mockito.verify(userRepository).findUserByUsername(username);
        Mockito.verify(traineeRepository).findTraineeByUser(user);

    }

    @Test
    void getTraineeProfile_UserNotFound() {
        String username = "nonexistent.user@example.com";
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> traineeService.getTraineeProfile(username));

        Mockito.verify(userRepository).findUserByUsername(username);
        Mockito.verifyNoMoreInteractions(traineeRepository);
    }
    @Test
    void getTraineeProfile_TraineeNotFound() {
        String username = "john.doe@example.com";
        User user = ObjectPreparation.createValidUser();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        Mockito.when(traineeRepository.findTraineeByUser(Mockito.any(User.class))).thenReturn(Optional.empty());

        assertThrows(TraineeException.class, () -> traineeService.getTraineeProfile(username));


        Mockito.verify(userRepository).findUserByUsername(username);
        Mockito.verify(traineeRepository).findTraineeByUser(user);
    }
    @Test
    void updateTraineeProfile_Success(){
        String username = "john.doe@example.com";
        String updatedFirstName = "Osama";
        User user = ObjectPreparation.createValidUser();
        user.setFirstName(updatedFirstName);
        Trainee trainee = ObjectPreparation.createValidTrainee();
        trainee.setUser(user);
        TraineeProfileDto traineeProfileDto = ObjectPreparation.createValidTraineeProfileDto();
        traineeProfileDto.setFirstName(updatedFirstName);
        TraineeUpdateRequestDto traineeUpdateRequestDto = ObjectPreparation.createValidTraineeUpdateRequestDto();
        traineeUpdateRequestDto.setFirstName(updatedFirstName);
        NotificationDto notificationDto = NotificationDto.builder().emailInfo(new HashMap<String, String>() {{
                    put("firstName", "Osama");
                    put("lastName", "Doe");
                    put("address", "123 Main Street");
                    put("dateOfBirth", "2000-01-01");
                    put("username", "john.doe@example.com");
                }}).emailType("Trainee_Update")
                .to(user.getUsername()).build();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(traineeRepository.findTraineeByUser(user)).thenReturn(Optional.of(trainee));
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.doNothing().when(kafkaNotificationProducerService).sendMessage(notificationDto);
        TraineeProfileDto result = traineeService.updateTraineeProfile(username,traineeUpdateRequestDto);
        assertEquals(traineeProfileDto,result);
        Mockito.verify(traineeRepository, Mockito.times(1)).save(eq(trainee));
        Mockito.verify(kafkaNotificationProducerService, Mockito.times(1)).sendMessage(eq(notificationDto));
    }

    @Test
    void updateTraineeProfile_UserNotFound() {
        String username = "nonexistent.user@example.com";
        TraineeUpdateRequestDto traineeUpdateRequestDto = ObjectPreparation.createValidTraineeUpdateRequestDto();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> traineeService.updateTraineeProfile(username, traineeUpdateRequestDto));

        Mockito.verify(traineeRepository, Mockito.never()).save(any(Trainee.class));
        Mockito.verify(kafkaNotificationProducerService, Mockito.never()).sendMessage(any(NotificationDto.class));
    }

    @Test
    void updateTraineeProfile_TraineeNotFound() {
        String username = "john.doe@example.com";
        TraineeUpdateRequestDto traineeUpdateRequestDto = ObjectPreparation.createValidTraineeUpdateRequestDto();
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(new User()));

        Mockito.when(traineeRepository.findTraineeByUser(Mockito.any(User.class))).thenReturn(Optional.empty());

        assertThrows(TraineeException.class, () -> traineeService.updateTraineeProfile(username, traineeUpdateRequestDto));

        Mockito.verify(traineeRepository, Mockito.never()).save(any(Trainee.class));
        Mockito.verify(kafkaNotificationProducerService, Mockito.never()).sendMessage(any(NotificationDto.class));
    }

    @Test
    void assignTrainers_Success(){
        AssignTrainersRequestDto assignTrainersRequestDto = ObjectPreparation.createValidAssignTrainersRequestDto();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Set<Trainer> currentTrainer = new HashSet<>();
        List<Trainer> trainers = List.of(ObjectPreparation.createValidTrainer());
        List<TrainerResponseDto> trainerResponseDtoList = List.of(ObjectPreparation.createValidTrainerResponseDto());
        Mockito.when(traineeRepository.findByUserUsername(assignTrainersRequestDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.when(trainerRepository.findByUserUsername("john.trainer.doe@example.com")).thenReturn(Optional.of(trainers.get(0)));
        List<TrainerResponseDto> result = traineeService.assignTrainers(assignTrainersRequestDto);
        assertEquals(trainerResponseDtoList,result);

    }

    @Test
    void assignTrainers_TraineeNotFound() {
        AssignTrainersRequestDto assignTrainersRequestDto = ObjectPreparation.createValidAssignTrainersRequestDto();

        Mockito.when(traineeRepository.findByUserUsername(eq(assignTrainersRequestDto.getTraineeUsername())))
                .thenReturn(Optional.empty());
        assertThrows(TraineeException.class, () -> traineeService.assignTrainers(assignTrainersRequestDto));

        Mockito.verify(trainerRepository, Mockito.never()).findByUserUsername(any(String.class));
    }

    @Test
    void assignTrainers_TrainerNotFound() {
        AssignTrainersRequestDto assignTrainersRequestDto = ObjectPreparation.createValidAssignTrainersRequestDto();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Mockito.when(traineeRepository.findByUserUsername(eq(assignTrainersRequestDto.getTraineeUsername())))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(eq("john.trainer.doe@example.com")))
                .thenReturn(Optional.empty());
        assertThrows(TrainerException.class, () -> traineeService.assignTrainers(assignTrainersRequestDto));
        Mockito.verify(trainerRepository, Mockito.times(1)).findByUserUsername(any(String.class));
    }

    @Test
    void assignTrainers_TrainerAlreadyAssigned() {
        AssignTrainersRequestDto assignTrainersRequestDto = ObjectPreparation.createValidAssignTrainersRequestDto();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();

        Set<Trainer> currentTrainers = new HashSet<>();
        currentTrainers.add(trainer);
        trainee.setTrainers(currentTrainers);
        Mockito.when(traineeRepository.findByUserUsername(eq(assignTrainersRequestDto.getTraineeUsername())))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(eq("john.trainer.doe@example.com")))
                .thenReturn(Optional.of(trainer));
        List<TrainerResponseDto> result = traineeService.assignTrainers(assignTrainersRequestDto);
        assertTrue(!result.isEmpty());
        Mockito.verify(traineeRepository, Mockito.times(1)).save(any(Trainee.class));
    }

    @Test
    void deleteTrainee_Success(){
        User user = ObjectPreparation.createValidUser();
        Trainee trainee = ObjectPreparation.createValidTrainee();

        Mockito.when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(traineeRepository.findTraineeByUser(user)).thenReturn(Optional.of(trainee));

        assertDoesNotThrow(() -> traineeService.deleteTrainee(user.getUsername()));
        Mockito.verify(userRepository).findUserByUsername(user.getUsername());
        Mockito.verify(traineeRepository).findTraineeByUser(user);

    }

    @Test
    public void testDeleteTrainee_UserNotFound() {

        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(java.util.Optional.empty());

        assertThrows(UserException.class, () -> traineeService.deleteTrainee("john.doe@example.com"));

        Mockito.verify(traineeRepository, Mockito.never()).delete(any(Trainee.class));
    }

    @Test
    public void testDeleteTrainee_TraineeNotFound() {

        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(java.util.Optional.of(new User()));

        Mockito.when(traineeRepository.findTraineeByUser(Mockito.any(User.class))).thenReturn(java.util.Optional.empty());

        assertThrows(TraineeException.class, () -> traineeService.deleteTrainee("john.doe@example.com"));

        Mockito.verify(traineeRepository, Mockito.never()).delete(any(Trainee.class));
    }

}
