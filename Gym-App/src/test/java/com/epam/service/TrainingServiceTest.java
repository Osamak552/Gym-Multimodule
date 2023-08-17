package com.epam.service;

import com.epam.ObjectPreparation;
import com.epam.dtos.request.TraineeTrainingsDto;
import com.epam.dtos.request.TrainerTrainingsDto;
import com.epam.dtos.request.TrainingRequestDto;
import com.epam.dtos.response.TrainingResponseDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import com.epam.exceptions.TraineeException;
import com.epam.exceptions.TrainerException;
import com.epam.exceptions.TrainingException;
import com.epam.repositories.TraineeRepository;
import com.epam.repositories.TrainerRepository;
import com.epam.repositories.TrainingRepository;
import com.epam.repositories.TrainingTypeRepository;
import com.epam.services.KafkaNotificationProducerService;
import com.epam.services.KafkaReportProducerService;
import com.epam.services.implementations.TrainingServiceImpl;
import com.epam.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;


@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {

    @Mock
    TrainerRepository trainerRepository;
    @Mock
    TraineeRepository traineeRepository;
    @Mock
    TrainingTypeRepository trainingTypeRepository;
    @Mock
    TrainingRepository trainingRepository;
    @Mock
    KafkaNotificationProducerService kafkaNotificationProducerService;
    @Mock
    KafkaReportProducerService kafkaReportProducerService;

    @InjectMocks
    TrainingServiceImpl trainingService;

    @Test
    void createTraining_Success() {
        TrainingRequestDto trainingRequestDto = ObjectPreparation.createValidTrainingRequestDto();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        trainee.getTrainers().add(trainer);
        trainer.getTrainees().add(trainee);
        Training training = ObjectPreparation.createValidTraining();


        Mockito.when(trainerRepository.findByUserUsername(trainingRequestDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainingRequestDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainingRepository.save(any())).thenReturn(training);


        assertDoesNotThrow(() -> trainingService.createTraining(trainingRequestDto));


        Mockito.verify(trainerRepository).findByUserUsername(eq(trainingRequestDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(eq(trainingRequestDto.getTraineeUsername()));
        Mockito.verify(trainingRepository).save(any());
        Mockito.verify(kafkaReportProducerService).sendReport(any());
        Mockito.verify(kafkaNotificationProducerService).sendMessage(any());
    }

    @Test
    void createTraining_TrainerNotFound() {
        TrainingRequestDto trainingRequestDto = ObjectPreparation.createValidTrainingRequestDto();


        Mockito.when(trainerRepository.findByUserUsername(trainingRequestDto.getTrainerUsername())).thenReturn(Optional.empty());


        assertThrows(TrainerException.class, () -> trainingService.createTraining(trainingRequestDto));


        Mockito.verify(trainerRepository).findByUserUsername(eq(trainingRequestDto.getTrainerUsername()));
        Mockito.verifyNoMoreInteractions(traineeRepository, trainingRepository, kafkaReportProducerService, kafkaNotificationProducerService);
    }

    @Test
    void createTraining_TraineeNotFound() {
        TrainingRequestDto trainingRequestDto = ObjectPreparation.createValidTrainingRequestDto();
        Trainer trainer = ObjectPreparation.createValidTrainer();

        Mockito.when(trainerRepository.findByUserUsername(trainingRequestDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainingRequestDto.getTraineeUsername())).thenReturn(Optional.empty());

        assertThrows(TraineeException.class, () -> trainingService.createTraining(trainingRequestDto));

        Mockito.verify(trainerRepository).findByUserUsername(eq(trainingRequestDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(eq(trainingRequestDto.getTraineeUsername()));
        Mockito.verifyNoMoreInteractions(trainingRepository, kafkaReportProducerService, kafkaNotificationProducerService);
    }

    @Test
    void createTraining_TraineeNotAssignedWithTrainer() {
        TrainingRequestDto trainingRequestDto = ObjectPreparation.createValidTrainingRequestDto();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();


        Mockito.when(trainerRepository.findByUserUsername(trainingRequestDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainingRequestDto.getTraineeUsername())).thenReturn(Optional.of(trainee));


        assertThrows(TrainingException.class, () -> trainingService.createTraining(trainingRequestDto));


        Mockito.verify(trainerRepository).findByUserUsername(eq(trainingRequestDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(eq(trainingRequestDto.getTraineeUsername()));
        Mockito.verifyNoMoreInteractions(trainingRepository, kafkaReportProducerService, kafkaNotificationProducerService);
    }


    @Test
    void getTraineeTrainings_WithAllFilters_Success() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .periodTo(LocalDate.parse("2024-09-15"))
                .trainingType("Fitness")
                .build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainingType trainingType = TrainingType.builder().trainingTypeId(1l).trainingTypeName("Fitness").build();
        List<Training> trainings = List.of(ObjectPreparation.createValidTraining());
        List<TrainingResponseDto> trainingResponseDto = List.of(ObjectPreparation.createTrainingResponseDto());
        // Mock behaviors
        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(traineeTrainingsDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(traineeTrainingsDto.getTrainingType())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), trainer, trainee, trainingType))
                .thenReturn(trainings);

        List<TrainingResponseDto> result = trainingService.getTraineeTrainings(traineeTrainingsDto);
        assertEquals(trainingResponseDto.size(), result.size());

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainerRepository).findByUserUsername(eq(traineeTrainingsDto.getTrainerUsername()));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(traineeTrainingsDto.getTrainingType()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(traineeTrainingsDto.getPeriodFrom()), eq(traineeTrainingsDto.getPeriodTo()), eq(trainer), eq(trainee), eq(trainingType));
        Mockito.verifyNoMoreInteractions(traineeRepository, trainerRepository, trainingTypeRepository, trainingRepository);
    }


    @Test
    void getTraineeTrainings_WithoutPeriodFrom_Success() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodTo(LocalDate.parse("2024-09-15"))
                .trainingType("Fitness")
                .build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainingType trainingType = TrainingType.builder().trainingTypeId(1l).trainingTypeName("Fitness").build();
        List<Training> trainings = List.of(ObjectPreparation.createValidTraining());
        List<TrainingResponseDto> trainingResponseDto = List.of(ObjectPreparation.createTrainingResponseDto());
        // Mock behaviors
        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(traineeTrainingsDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(traineeTrainingsDto.getTrainingType())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), trainer, trainee, trainingType))
                .thenReturn(trainings);

        List<TrainingResponseDto> result = trainingService.getTraineeTrainings(traineeTrainingsDto);
        assertEquals(trainingResponseDto.size(), result.size());

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainerRepository).findByUserUsername(eq(traineeTrainingsDto.getTrainerUsername()));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(traineeTrainingsDto.getTrainingType()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(traineeTrainingsDto.getPeriodFrom()), eq(traineeTrainingsDto.getPeriodTo()), eq(trainer), eq(trainee), eq(trainingType));
        Mockito.verifyNoMoreInteractions(traineeRepository, trainerRepository, trainingTypeRepository, trainingRepository);
    }

    @Test
    void getTraineeTrainings_WithoutPeriodTo_Success() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .trainingType("Fitness")
                .build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainingType trainingType = TrainingType.builder().trainingTypeId(1l).trainingTypeName("Fitness").build();
        List<Training> trainings = List.of(ObjectPreparation.createValidTraining());
        List<TrainingResponseDto> trainingResponseDto = List.of(ObjectPreparation.createTrainingResponseDto());
        // Mock behaviors
        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(traineeTrainingsDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(traineeTrainingsDto.getTrainingType())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), trainer, trainee, trainingType))
                .thenReturn(trainings);

        List<TrainingResponseDto> result = trainingService.getTraineeTrainings(traineeTrainingsDto);
        assertEquals(trainingResponseDto.size(), result.size());

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainerRepository).findByUserUsername(eq(traineeTrainingsDto.getTrainerUsername()));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(traineeTrainingsDto.getTrainingType()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(traineeTrainingsDto.getPeriodFrom()), eq(traineeTrainingsDto.getPeriodTo()), eq(trainer), eq(trainee), eq(trainingType));
        Mockito.verifyNoMoreInteractions(traineeRepository, trainerRepository, trainingTypeRepository, trainingRepository);
    }


    @Test
    void getTraineeTrainings_WithoutTrainerAndTrainingType_Success() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUsername("john.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .periodTo(LocalDate.parse("2024-09-15"))
                .build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        List<Training> trainings = List.of(ObjectPreparation.createValidTraining());

        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), null, trainee, null))
                .thenReturn(trainings);

        List<TrainingResponseDto> result = trainingService.getTraineeTrainings(traineeTrainingsDto);

        assertEquals(trainings.size(), result.size());

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(traineeTrainingsDto.getPeriodFrom()), eq(traineeTrainingsDto.getPeriodTo()), isNull(), eq(trainee), isNull());
        Mockito.verifyNoMoreInteractions(traineeRepository, trainingRepository);
    }

    @Test
    void getTraineeTrainings_InvalidTraineeUsername_ThrowsTraineeException() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .traineeUsername("invalid.username@gmail.com")
                .build();

        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenThrow(new TraineeException(Constants.TRAINEE_NOT_FOUND));

        assertThrows(TraineeException.class, () -> trainingService.getTraineeTrainings(traineeTrainingsDto));

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verifyNoMoreInteractions(traineeRepository);
    }

    @Test
    void getTraineeTrainings_WithoutPeriodFromAndPeriodTo() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .trainingType("Fitness")
                .build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainingType trainingType = TrainingType.builder().trainingTypeId(1l).trainingTypeName("Fitness").build();
        List<Training> trainings = List.of(ObjectPreparation.createValidTraining());
        List<TrainingResponseDto> trainingResponseDto = List.of(ObjectPreparation.createTrainingResponseDto());

        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(traineeTrainingsDto.getTrainerUsername())).thenReturn(Optional.of(trainer));
        Mockito.when(trainingTypeRepository.findTrainingTypeByTrainingTypeName(traineeTrainingsDto.getTrainingType())).thenReturn(Optional.of(trainingType));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), trainer, trainee, trainingType))
                .thenReturn(trainings);


        List<TrainingResponseDto> result = trainingService.getTraineeTrainings(traineeTrainingsDto);
        assertEquals(trainingResponseDto.size(), result.size());

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainerRepository).findByUserUsername(eq(traineeTrainingsDto.getTrainerUsername()));
        Mockito.verify(trainingTypeRepository).findTrainingTypeByTrainingTypeName(eq(traineeTrainingsDto.getTrainingType()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(traineeTrainingsDto.getPeriodFrom()), eq(traineeTrainingsDto.getPeriodTo()), eq(trainer), eq(trainee), eq(trainingType));
        Mockito.verifyNoMoreInteractions(traineeRepository, trainerRepository, trainingTypeRepository, trainingRepository);
    }

    @Test
    void getTraineeTrainings_WithoutTrainingType() {
        TraineeTrainingsDto traineeTrainingsDto = TraineeTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .periodTo(LocalDate.parse("2024-09-15"))
                .build();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Trainer trainer = ObjectPreparation.createValidTrainer();
        TrainingType trainingType = null;
        List<Training> trainingList = List.of(ObjectPreparation.createValidTraining());


        Mockito.when(traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())).thenReturn(Optional.of(trainee));
        Mockito.when(trainerRepository.findByUserUsername(traineeTrainingsDto.getTrainerUsername())).thenReturn(Optional.of(trainer));

        Mockito.when(trainingRepository.findAllTrainingInBetween(traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), trainer, trainee, trainingType)).thenReturn(trainingList);

        List<TrainingResponseDto> result = trainingService.getTraineeTrainings(traineeTrainingsDto);

        assertEquals(trainingList.size(), result.size());

        Mockito.verify(traineeRepository).findByUserUsername(eq(traineeTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainerRepository).findByUserUsername(eq(traineeTrainingsDto.getTrainerUsername()));

        Mockito.verify(trainingRepository).findAllTrainingInBetween(traineeTrainingsDto.getPeriodFrom(), traineeTrainingsDto.getPeriodTo(), trainer, trainee, trainingType);
        Mockito.verifyNoMoreInteractions(traineeRepository, trainerRepository, trainingTypeRepository, trainingRepository);
    }


    @Test
    void getTrainerTrainings_WithAllFilters_Success() {

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .periodTo(LocalDate.parse("2024-09-15"))
                .build();

        Trainer trainer = ObjectPreparation.createValidTrainer();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Training training = ObjectPreparation.createValidTraining();
        List<Training> trainings = new ArrayList<>();
        trainings.add(training);


        Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsDto.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainerTrainingsDto.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        trainerTrainingsDto.getPeriodFrom(),
                        trainerTrainingsDto.getPeriodTo(),
                        trainer, trainee))
                .thenReturn(trainings);


        List<TrainingResponseDto> result = trainingService.getTrainerTrainings(trainerTrainingsDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training.getTrainingName(), result.get(0).getTrainingName());

        Mockito.verify(trainerRepository).findByUserUsername(eq(trainerTrainingsDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(eq(trainerTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(trainerTrainingsDto.getPeriodFrom()),
                eq(trainerTrainingsDto.getPeriodTo()),
                eq(trainer), eq(trainee));

    }

    @Test
    void getTrainerTrainings_WithoutTrainee_Success() {
        // Prepare test data
        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .periodTo(LocalDate.parse("2024-09-15"))
                .build();

        Trainer trainer = ObjectPreparation.createValidTrainer();
        Training training = ObjectPreparation.createValidTraining();
        List<Training> trainings = new ArrayList<>();
        trainings.add(training);

        Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsDto.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        trainerTrainingsDto.getPeriodFrom(),
                        trainerTrainingsDto.getPeriodTo(),
                        trainer, null))
                .thenReturn(trainings);

        List<TrainingResponseDto> result = trainingService.getTrainerTrainings(trainerTrainingsDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training.getTrainingName(), result.get(0).getTrainingName());

        Mockito.verify(trainerRepository).findByUserUsername(eq(trainerTrainingsDto.getTrainerUsername()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(trainerTrainingsDto.getPeriodFrom()),
                eq(trainerTrainingsDto.getPeriodTo()),
                eq(trainer), isNull());

    }

    @Test
    void getTrainerTrainings_WithoutPeriodFromAndPeriodTo_Success() {
        // Prepare test data
        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .build();

        Trainer trainer = ObjectPreparation.createValidTrainer();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Training training = ObjectPreparation.createValidTraining();
        List<Training> trainings = new ArrayList<>();
        trainings.add(training);

        // Mock behavior
        Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsDto.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainerTrainingsDto.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        trainerTrainingsDto.getPeriodFrom(),trainerTrainingsDto.getPeriodTo(), trainer, trainee))
                .thenReturn(trainings);

        // Execute the method
        List<TrainingResponseDto> result = trainingService.getTrainerTrainings(trainerTrainingsDto);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.size());

        Mockito.verify(trainerRepository).findByUserUsername(eq(trainerTrainingsDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(trainerTrainingsDto.getTraineeUsername());
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(trainerTrainingsDto.getPeriodFrom()),
                eq(trainerTrainingsDto.getPeriodTo()),
                eq(trainer), eq(trainee));

    }

    @Test
    void getTrainerTrainings_WithoutPeriodTo_Success() {

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodFrom(LocalDate.parse("2023-07-15"))
                .build();

        Trainer trainer = ObjectPreparation.createValidTrainer();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Training training = ObjectPreparation.createValidTraining();
        List<Training> trainings = new ArrayList<>();
        trainings.add(training);


        Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsDto.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainerTrainingsDto.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        trainerTrainingsDto.getPeriodFrom(),
                        trainerTrainingsDto.getPeriodTo(),
                        trainer, trainee))
                .thenReturn(trainings);


        List<TrainingResponseDto> result = trainingService.getTrainerTrainings(trainerTrainingsDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training.getTrainingName(), result.get(0).getTrainingName());

        Mockito.verify(trainerRepository).findByUserUsername(eq(trainerTrainingsDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(eq(trainerTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(trainerTrainingsDto.getPeriodFrom()),
                eq(trainerTrainingsDto.getPeriodTo()),
                eq(trainer), eq(trainee));

    }
    @Test
    void getTrainerTrainings_WithoutPeriodFrom_Success() {

        TrainerTrainingsDto trainerTrainingsDto = TrainerTrainingsDto.builder()
                .trainerUsername("john.trainer.doe@example.com")
                .traineeUsername("john.doe@example.com")
                .periodTo(LocalDate.parse("2024-09-15"))
                .build();

        Trainer trainer = ObjectPreparation.createValidTrainer();
        Trainee trainee = ObjectPreparation.createValidTrainee();
        Training training = ObjectPreparation.createValidTraining();
        List<Training> trainings = new ArrayList<>();
        trainings.add(training);


        Mockito.when(trainerRepository.findByUserUsername(trainerTrainingsDto.getTrainerUsername()))
                .thenReturn(Optional.of(trainer));
        Mockito.when(traineeRepository.findByUserUsername(trainerTrainingsDto.getTraineeUsername()))
                .thenReturn(Optional.of(trainee));
        Mockito.when(trainingRepository.findAllTrainingInBetween(
                        trainerTrainingsDto.getPeriodFrom(),
                        trainerTrainingsDto.getPeriodTo(),
                        trainer, trainee))
                .thenReturn(trainings);


        List<TrainingResponseDto> result = trainingService.getTrainerTrainings(trainerTrainingsDto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(training.getTrainingName(), result.get(0).getTrainingName());

        Mockito.verify(trainerRepository).findByUserUsername(eq(trainerTrainingsDto.getTrainerUsername()));
        Mockito.verify(traineeRepository).findByUserUsername(eq(trainerTrainingsDto.getTraineeUsername()));
        Mockito.verify(trainingRepository).findAllTrainingInBetween(
                eq(trainerTrainingsDto.getPeriodFrom()),
                eq(trainerTrainingsDto.getPeriodTo()),
                eq(trainer), eq(trainee));

    }



}
