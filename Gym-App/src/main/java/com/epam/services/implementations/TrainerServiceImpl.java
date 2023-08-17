package com.epam.services.implementations;

import com.epam.dtos.request.NotificationDto;
import com.epam.repositories.TraineeRepository;
import com.epam.repositories.TrainerRepository;
import com.epam.repositories.TrainingTypeRepository;
import com.epam.repositories.UserRepository;
import com.epam.dtos.request.TrainerRequestDto;
import com.epam.dtos.request.TrainerUpdateRequestDto;
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

import com.epam.services.KafkaNotificationProducerService;
import com.epam.services.TrainerService;
import com.epam.util.Constants;
import com.epam.util.NotificationConvertor;
import com.epam.util.ObjectConvertor;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class TrainerServiceImpl implements TrainerService {

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaNotificationProducerService kafkaNotificationProducerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public TrainerResponseDto createTrainer(TrainerRequestDto trainerRequestDto)
    {
        log.info("{}:: createTrainer {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TrainingType trainingType = trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainerRequestDto.getSpecialization())
                .orElseThrow(() -> new TrainingTypeException("Specialization not found in the database"));
        User user = ObjectConvertor.getUserFromTrainerDto(trainerRequestDto);
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Trainer trainer = Trainer.builder()
                .specialization(trainingType)
                .user(user)
                .build();
        user.setTrainer(trainer);
        trainer = trainerRepository.save(trainer);
        TrainerResponseDto trainerResponseDto = TrainerResponseDto.builder()
                .username(trainer.getUser().getUsername())
                .password(trainer.getUser().getPassword())
                .build();
        NotificationDto notificationDto = NotificationConvertor.getNotificationDto(user.getUsername(),password);
        kafkaNotificationProducerService.sendMessage(notificationDto);
        log.info("{}:: createTrainer {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        trainerResponseDto.setPassword(password);
        return trainerResponseDto;
    }

    public TrainerProfileDto getTrainerProfile(String username)
    {
        log.info("{}:: getTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserException(Constants.USER_NOT_FOUND));
        Trainer trainer = trainerRepository.findTrainerByUser(user).orElseThrow(() -> new TrainerException(Constants.TRAINER_NOT_FOUND));
        TrainerProfileDto trainerProfileDto = ObjectConvertor.convertToTrainerProfileDto(trainer,user);
        log.info("{}:: getTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return trainerProfileDto;
    }

    @Transactional
    public TrainerProfileDto updateTrainerProfile(String username, TrainerUpdateRequestDto trainerUpdateRequestDto)
    {
        log.info("{}:: updateTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserException(Constants.USER_NOT_FOUND));
        TrainingType trainingType = trainingTypeRepository.findTrainingTypeByTrainingTypeName(trainerUpdateRequestDto.getSpecialization())
                .orElseThrow(() -> new TrainingTypeException(Constants.TRAINING_TYPE_NOT_FOUND));
        Trainer trainer = trainerRepository.findTrainerByUser(user).orElseThrow(() -> new TrainerException(Constants.TRAINER_NOT_FOUND));
        user.setFirstName(trainerUpdateRequestDto.getFirstName());
        user.setLastName(trainerUpdateRequestDto.getLastName());
        user.setActive(trainerUpdateRequestDto.isActive());
        trainer.setSpecialization(trainingType);
        trainer.setUser(user);
        trainerRepository.save(trainer);

        NotificationDto notificationDto = NotificationConvertor.getNotificationDto(trainer);
        kafkaNotificationProducerService.sendMessage(notificationDto);
        log.info("{}:: updateTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return ObjectConvertor.convertToTrainerProfileDto(trainer,user);
    }

    public List<TrainerResponseDto> getUnassignedTrainersFromTrainee(String traineeUsername)
    {
        log.info("{}:: getUnassignedTrainersFromTrainee {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        Trainee trainee = traineeRepository.findByUserUsername(traineeUsername).orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));
        List<Trainer> trainers = trainerRepository.findActiveTrainersNotAssignedToTrainee(trainee);
        List<TrainerResponseDto> trainerResponseDtoList = trainers.stream()
                                                            .map(ObjectConvertor::trainerToTrainerDto).toList();
        log.info("{}:: getUnassignedTrainersFromTrainee {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return trainerResponseDtoList;
    }



}
