package com.epam;

import com.epam.dtos.request.*;
import com.epam.dtos.response.*;
import com.epam.entities.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ObjectPreparation {
    public static User createValidUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("john.doe@example.com");
        user.setEmail("john.doe@example.com");
        user.setPassword("12345");
        user.setActive(true);
        user.setCreatedAt(new Date());
        return user;
    }

    public static Trainee createValidTrainee(){
        User user = createValidUser();
        return Trainee.builder()
                .user(user) // Set the user object
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("123 Main Street")
                .trainers(new HashSet<>())
                .trainings(new HashSet<>())
                .build();
    }

    public static Trainer createValidTrainer(){
        User user = createValidUser();
        user.setUsername("john.trainer.doe@example.com");
        return Trainer.builder()
                .user(user)
                .specialization(TrainingType.builder().trainingTypeId(1l).trainingTypeName("Fitness").build())
                .trainees(new HashSet<>())
                .trainings(new HashSet<>())
                .build();
    }
    public static TraineeRequestDto createValidTraineeRequestDto() {
        TraineeRequestDto dto = new TraineeRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setAddress("123 Main Street");
        return dto;
    }

    public static TraineeResponseDto createValidTraineeResponseDto() {
        return TraineeResponseDto.builder()
                .username("john.doe@example.com")
                .password("12345")
                .build();
    }

    public static TraineeProfileDto createValidTraineeProfileDto() {
        return TraineeProfileDto.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("123 Main Street")
                .isActive(true)
                .trainerResponseDtoList(new ArrayList<TrainerResponseDto>()) // Provide a list of TrainerResponseDto objects
                .build();
    }

    public static TraineeUpdateRequestDto createValidTraineeUpdateRequestDto() {
        TraineeUpdateRequestDto dto = new TraineeUpdateRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
        dto.setAddress("123 Main Street");
        dto.setActive(true);
        return dto;
    }

    public static AssignTrainersRequestDto createValidAssignTrainersRequestDto() {
        AssignTrainersRequestDto dto = new AssignTrainersRequestDto();
        dto.setTraineeUsername("john.doe@example.com");
        List<String> trainerUsernameList = new ArrayList<>();
        trainerUsernameList.add("john.trainer.doe@example.com");
        dto.setTrainerUsernameList(trainerUsernameList);
        return dto;
    }


    public static TrainerResponseDto createValidTrainerResponseDto() {
        return TrainerResponseDto.builder()
                .firstName("John")
                .lastName("Doe")
                .username("john.trainer.doe@example.com")
                .specialization("Fitness")
                .build();
    }

    public static LoginCredential createValidLoginCredential() {
        return new LoginCredential("john.doe@example.com", "12345");
    }

    public static ChangePasswordDto createValidChangePassword() {
        return new ChangePasswordDto("john.doe@example.com", "12345", "newpassword");
    }


    public static TrainerRequestDto createValidTrainerRequestDto() {
        TrainerRequestDto dto = new TrainerRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.trainer.doe@example.com");
        dto.setSpecialization("Fitness");
        return dto;
    }

    public static TrainerResponseDto createTrainerResponseDto() {
        TrainerResponseDto dto = TrainerResponseDto.builder()
                .username("john.trainer.doe@example.com")
                .password("12345")
                .build();
        return dto;
    }

    public static TrainerProfileDto createValidTrainerProfileDto() {
        return TrainerProfileDto.builder()
                .firstName("John")
                .lastName("Doe")
                .specialization("Fitness")
                .isActive(true)
                .traineeResponseDtoList(new ArrayList<TraineeResponseDto>())
                .build();
    }

    public static TrainerUpdateRequestDto createValidTrainerUpdateRequestDto() {
        TrainerUpdateRequestDto dto = new TrainerUpdateRequestDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setSpecialization("Fitness");
        dto.setActive(true);
        return dto;
    }

    public static TrainingRequestDto createValidTrainingRequestDto() {
        TrainingRequestDto dto = new TrainingRequestDto();
        dto.setTraineeUsername("john.doe@example.com");
        dto.setTrainerUsername("john.trainer.doe@example.com");
        dto.setTrainingName("Fitness Training");
        dto.setTrainingDate(LocalDate.parse("2023-10-15"));
        dto.setTrainingDuration(60);
        return dto;
    }

    public static TrainingResponseDto createTrainingResponseDto() {
        return TrainingResponseDto.builder()
                .trainingName("Fitness Training")
                .trainingType("Fitness")
                .trainerName("John Doe")
                .traineeName("Alice Johnson")
                .trainingDuration(60)
                .trainingDate(LocalDate.parse("2024-08-15"))
                .build();
    }


    public static Training createValidTraining() {
        return Training.builder()
                .trainee(createValidTrainee())
                .trainer(createValidTrainer())
                .trainingName("Fitness Training")
                .trainingType(TrainingType.builder().trainingTypeId(1l).trainingTypeName("Fitness").build())
                .trainingDate(LocalDate.parse("2024-08-15"))
                .trainingDuration(50)
                .build();
    }
}

