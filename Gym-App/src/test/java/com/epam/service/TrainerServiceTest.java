package com.epam.service;

import com.epam.ObjectPreparation;
import com.epam.dtos.request.*;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.exceptions.TraineeException;
import com.epam.exceptions.TrainerException;
import com.epam.exceptions.TrainingTypeException;
import com.epam.exceptions.UserException;
import com.epam.repositories.TraineeRepository;
import com.epam.repositories.TrainerRepository;
import com.epam.repositories.TrainingTypeRepository;
import com.epam.repositories.UserRepository;
import com.epam.services.KafkaNotificationProducerService;
import com.epam.services.implementations.TraineeServiceImpl;
import com.epam.services.implementations.TrainerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TrainerServiceTest {
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    TraineeRepository traineeRepository;
    @Mock
    TrainingTypeRepository trainingTypeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    KafkaNotificationProducerService kafkaNotificationProducerService;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    TrainerServiceImpl trainerService;

    @Test
    void createTrainer_Success() {
        TrainerRequestDto trainerRequestDto = ObjectPreparation.createValidTrainerRequestDto();
        TrainingType trainingType = TrainingType.builder()
                .trainingTypeId(1L)
                .trainingTypeName("Fitness")
                .build();
        User user = ObjectPreparation.createValidUser();
        user.setEmail("john.trainer.doe@example.com");
        user.setUsername("john.trainer.doe@example.com");
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainerResponseDto expectedResponse = TrainerResponseDto.builder().username("john.trainer.doe@example.com").password("12345").build();
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainerRequestDto.getSpecialization()))
                .thenReturn(Optional.of(trainingType));
        Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        TrainerResponseDto result = trainerService.createTrainer(trainerRequestDto);
        assertEquals(expectedResponse,result);

        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(trainerRequestDto.getSpecialization()));
        Mockito.verify(trainerRepository).save(any(Trainer.class));
        Mockito.verify(kafkaNotificationProducerService).sendMessage(any(NotificationDto.class));
    }

    @Test
    void getTrainerProfile_Success() {
        String username = "john.trainer.doe@example.com";
        User user = ObjectPreparation.createValidUser();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainerProfileDto expectedProfile = ObjectPreparation.createValidTrainerProfileDto();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(trainerRepository.findTrainerByUser(user)).thenReturn(Optional.of(trainer));

        TrainerProfileDto result = trainerService.getTrainerProfile(username);

        assertEquals(expectedProfile, result);
        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verify(trainerRepository).findTrainerByUser(eq(user));
    }

    @Test
    void getTrainerProfile_UserNotFound() {
        String username = "nonexistent.trainer@example.com";

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UserException.class, () -> trainerService.getTrainerProfile(username));

        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verifyNoMoreInteractions(trainerRepository, kafkaNotificationProducerService);
    }

    @Test
    void getTrainerProfile_TrainerNotFound() {
        String username = "john.trainer.doe@example.com";
        User user = ObjectPreparation.createValidUser();


        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        Mockito.when(trainerRepository.findTrainerByUser(user)).thenReturn(Optional.empty());

        assertThrows(TrainerException.class, () -> trainerService.getTrainerProfile(username));

        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verify(trainerRepository).findTrainerByUser(eq(user));
        Mockito.verifyNoMoreInteractions(kafkaNotificationProducerService);
    }
    @Test
    void updateTrainerProfile_Success() {
        String username = "john.trainer.doe@example.com";
        TrainerUpdateRequestDto trainerUpdateRequestDto = ObjectPreparation.createValidTrainerUpdateRequestDto();
        trainerUpdateRequestDto.setFirstName("Updated");
        User user = ObjectPreparation.createValidUser();
        user.setUsername(username);
        user.setEmail(username);
        TrainingType trainingType = TrainingType.builder()
                .trainingTypeId(1L)
                .trainingTypeName("Fitness")
                .build();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainerProfileDto expectedProfile = ObjectPreparation.createValidTrainerProfileDto();
        expectedProfile.setFirstName("Updated");
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainerUpdateRequestDto.getSpecialization())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainerRepository.findTrainerByUser(user)).thenReturn(Optional.of(trainer));
        Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);

        TrainerProfileDto result = trainerService.updateTrainerProfile(username, trainerUpdateRequestDto);

        assertEquals(expectedProfile, result);

        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(trainerUpdateRequestDto.getSpecialization()));
        Mockito.verify(trainerRepository).findTrainerByUser(eq(user));
        Mockito.verify(trainerRepository).save(any(Trainer.class));
        Mockito.verify(kafkaNotificationProducerService).sendMessage(any(NotificationDto.class));
    }

    @Test
    void updateTrainerProfile_UserNotFound() {
        String username = "nonexistent.trainer@example.com";
        TrainerUpdateRequestDto trainerUpdateRequestDto = ObjectPreparation.createValidTrainerUpdateRequestDto();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserException.class, () -> trainerService.updateTrainerProfile(username, trainerUpdateRequestDto));

        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verifyNoMoreInteractions(trainingTypeRepository, trainerRepository, kafkaNotificationProducerService);
    }

    @Test
    void updateTrainerProfile_TrainingTypeNotFound() {
        String username = "john.trainer.doe@example.com";
        TrainerUpdateRequestDto trainerUpdateRequestDto = ObjectPreparation.createValidTrainerUpdateRequestDto();
        User user = ObjectPreparation.createValidUser();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));

        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainerUpdateRequestDto.getSpecialization())).thenReturn(Optional.empty());

        assertThrows(TrainingTypeException.class, () -> trainerService.updateTrainerProfile(username, trainerUpdateRequestDto));

        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(trainerUpdateRequestDto.getSpecialization()));
        Mockito.verifyNoMoreInteractions(trainerRepository, kafkaNotificationProducerService);
    }

    @Test
    void updateTrainerProfile_TrainerNotFound() {
        String username = "john.trainer.doe@example.com";
        TrainerUpdateRequestDto trainerUpdateRequestDto = ObjectPreparation.createValidTrainerUpdateRequestDto();
        User user = ObjectPreparation.createValidUser();
        TrainingType trainingType = TrainingType.builder()
                .trainingTypeId(1L)
                .trainingTypeName("Updated Specialization")
                .build();

        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainerUpdateRequestDto.getSpecialization())).thenReturn(Optional.of(trainingType));

        Mockito.when(trainerRepository.findTrainerByUser(user)).thenReturn(Optional.empty());

        assertThrows(TrainerException.class, () -> trainerService.updateTrainerProfile(username, trainerUpdateRequestDto));

        Mockito.verify(userRepository).findUserByUsername(eq(username));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(trainerUpdateRequestDto.getSpecialization()));
        Mockito.verify(trainerRepository).findTrainerByUser(eq(user));
        Mockito.verifyNoMoreInteractions(kafkaNotificationProducerService);
    }

    @Test
    void getUnassignedTrainersFromTrainee_Success() {
        String traineeUsername = "john.doe@example.com";
        Trainee trainee = ObjectPreparation.createValidTrainee();
        List<Trainer> trainers = List.of(ObjectPreparation.createValidTrainer());
        List<TrainerResponseDto> expectedTrainerResponseDtoList = List.of(ObjectPreparation.createValidTrainerResponseDto());

        Mockito.when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findActiveTrainersNotAssignedToTrainee(trainee)).thenReturn(trainers);

        List<TrainerResponseDto> result = trainerService.getUnassignedTrainersFromTrainee(traineeUsername);

        assertEquals(expectedTrainerResponseDtoList, result);

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeUsername));
        Mockito.verify(trainerRepository).findActiveTrainersNotAssignedToTrainee(eq(trainee));
        Mockito.verifyNoMoreInteractions(kafkaNotificationProducerService);
    }

    @Test
    void getUnassignedTrainersFromTrainee_TraineeNotFound() {
        String traineeUsername = "nonexistent.trainee@example.com";
        Mockito.when(traineeRepository.findByUserUsername(traineeUsername)).thenReturn(Optional.empty());
        assertThrows(TraineeException.class, () -> trainerService.getUnassignedTrainersFromTrainee(traineeUsername));

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeUsername));
        Mockito.verifyNoMoreInteractions(trainerRepository, kafkaNotificationProducerService);
    }


}