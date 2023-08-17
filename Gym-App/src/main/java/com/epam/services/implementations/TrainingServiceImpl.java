package com.epam.services.implementations;

import com.epam.dtos.request.NotificationDto;
import com.epam.repositories.TraineeRepository;
import com.epam.repositories.TrainerRepository;
import com.epam.repositories.TrainingRepository;
import com.epam.repositories.TrainingTypeRepository;
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
import com.epam.exceptions.TrainingTypeException;
import com.epam.services.KafkaNotificationProducerService;
import com.epam.services.KafkaReportProducerService;
import com.epam.services.TrainingService;
import com.epam.util.Constants;
import com.epam.util.NotificationConvertor;
import com.epam.util.ObjectConvertor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private KafkaNotificationProducerService kafkaNotificationProducerService;
    @Autowired
    private KafkaReportProducerService kafkaReportProducerService;

    public void createTraining(TrainingRequestDto trainingRequestDto)
    {
        log.info("{}:: createTraining {}",this.getClass().getName(), Constants.EXECUTION_STARTED);

        Trainer trainer = trainerRepository.findByUserUsername(trainingRequestDto.getTrainerUsername())
                .orElseThrow(() -> new TrainerException(Constants.TRAINER_NOT_FOUND));
        Trainee trainee = traineeRepository.findByUserUsername(trainingRequestDto.getTraineeUsername())
                .orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));

        if(!trainee.getTrainers().contains(trainer))
        {
            throw new TrainingException("Trainee: "+trainee.getUser().getUsername()+" not assigned with trainer: " + trainer.getUser().getUsername());
        }

        Training training = Training.builder()
                .trainee(trainee)
                .trainer(trainer)
                .trainingName(trainingRequestDto.getTrainingName())
                .trainingDate(trainingRequestDto.getTrainingDate())
                .trainingType(trainer.getSpecialization())
                .trainingDuration(trainingRequestDto.getTrainingDuration())
                .build();
        training = trainingRepository.save(training);
        kafkaReportProducerService.sendReport(ObjectConvertor.convertToTrainerReportDto(training));
        NotificationDto notificationDto = NotificationConvertor.getNotificationDto(training);
        kafkaNotificationProducerService.sendMessage(notificationDto);
        log.info("{}:: createTraining {}",this.getClass().getName(), Constants.EXECUTION_ENDED);

    }

    public List<TrainingResponseDto> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto)
    {
        log.info("{}:: getTraineeTrainings {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        Trainee trainee = traineeRepository.findByUserUsername(traineeTrainingsDto.getTraineeUsername())
                .orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));
        Trainer trainer = Objects.isNull(traineeTrainingsDto.getTrainerUsername())?null:
                trainerRepository.findByUserUsername(traineeTrainingsDto.getTrainerUsername())
                .orElseThrow(() -> new TrainerException(Constants.TRAINER_NOT_FOUND));
        TrainingType trainingType = Objects.isNull(traineeTrainingsDto.getTrainingType())?null:
                trainingTypeRepository.findTrainingTypeByTrainingTypeName(traineeTrainingsDto.getTrainingType())
                        .orElseThrow(() -> new TrainingTypeException(Constants.TRAINING_TYPE_NOT_FOUND));

        List<Training> trainings = trainingRepository.findAllTrainingInBetween(traineeTrainingsDto.getPeriodFrom(),
                traineeTrainingsDto.getPeriodTo(),trainer,trainee,trainingType);

        log.info("{}:: getTraineeTrainings {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return trainings.stream().map(ObjectConvertor::trainingToTrainingResponseDto).toList();
    }

    public List<TrainingResponseDto> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto)
    {
        log.info("{}:: getTrainerTrainings {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        Trainer trainer = trainerRepository.findByUserUsername(trainerTrainingsDto.getTrainerUsername())
                .orElseThrow(() -> new TrainerException(Constants.TRAINER_NOT_FOUND));
        Trainee trainee = Objects.isNull(trainerTrainingsDto.getTraineeUsername())?null:
                traineeRepository.findByUserUsername(trainerTrainingsDto.getTraineeUsername())
                .orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));
        List<Training> trainings = trainingRepository.findAllTrainingInBetween(trainerTrainingsDto.getPeriodFrom(),
                trainerTrainingsDto.getPeriodTo(),trainer,trainee);
        log.info("{}:: getTrainerTrainings {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return trainings.stream().map(ObjectConvertor::trainingToTrainingResponseDto).toList();
    }



}
