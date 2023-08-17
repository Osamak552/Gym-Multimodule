package com.epam.services.implementations;

import com.epam.repositories.TraineeRepository;
import com.epam.repositories.TrainerRepository;
import com.epam.repositories.UserRepository;
import com.epam.dtos.request.*;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.User;
import com.epam.exceptions.TraineeException;
import com.epam.exceptions.TrainerException;
import com.epam.exceptions.UserException;
import com.epam.services.KafkaNotificationProducerService;
import com.epam.services.TraineeService;
import com.epam.util.Constants;
import com.epam.util.NotificationConvertor;
import com.epam.util.ObjectConvertor;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Slf4j
public class TraineeServiceImpl implements TraineeService {

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaNotificationProducerService kafkaNotificationProducerService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public TraineeResponseDto createTrainee(TraineeRequestDto traineeRequestDto)
    {
        log.info("{}:: createTrainee {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = ObjectConvertor.getUserFromTraineeDto(traineeRequestDto);
        String password = user.getPassword();
        Trainee trainee = ObjectConvertor.traineeDtoToTrainee(traineeRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        trainee.setUser(user);
        trainee.setUser(user);
        trainee = traineeRepository.save(trainee);
        TraineeResponseDto traineeResponseDto = TraineeResponseDto.builder()
                .username(trainee.getUser().getUsername())
                .password(trainee.getUser().getPassword())
                .build();
        NotificationDto notificationDto = NotificationConvertor.getNotificationDto(user.getUsername(),password);
        kafkaNotificationProducerService.sendMessage(notificationDto);
        log.info("{}:: createTrainee {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        traineeResponseDto.setPassword(password);
        return traineeResponseDto;
    }

    public TraineeProfileDto getTraineeProfile(String username)
    {
        log.info("{}:: getTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserException(Constants.USER_NOT_FOUND));
        Trainee trainee = traineeRepository.findTraineeByUser(user).orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));
        TraineeProfileDto traineeProfileDto = ObjectConvertor.convertToTraineeProfileDto(trainee,user);
        log.info("{}:: getTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return traineeProfileDto;
    }

    @Transactional
    public TraineeProfileDto updateTraineeProfile(String username, TraineeUpdateRequestDto traineeUpdateRequestDto)
    {
        log.info("{}:: updateTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserException(Constants.USER_NOT_FOUND));
        Trainee trainee = traineeRepository.findTraineeByUser(user).orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));
        user.setFirstName(traineeUpdateRequestDto.getFirstName());
        user.setLastName(traineeUpdateRequestDto.getLastName());
        user.setActive(traineeUpdateRequestDto.isActive());
        trainee.setAddress(traineeUpdateRequestDto.getAddress());
        trainee.setDateOfBirth(traineeUpdateRequestDto.getDateOfBirth());
        trainee.setUser(user);
        traineeRepository.save(trainee);
        NotificationDto notificationDto = NotificationConvertor.getNotificationDto(trainee);
        kafkaNotificationProducerService.sendMessage(notificationDto);
        log.info("{}:: updateTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return ObjectConvertor.convertToTraineeProfileDto(trainee,user);
    }

    @Transactional
    public List<TrainerResponseDto> assignTrainers(AssignTrainersRequestDto assignTrainersRequestDto)
    {
        log.info("{}:: assignTrainers {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        Trainee trainee = traineeRepository.findByUserUsername(assignTrainersRequestDto.getTraineeUsername())
                .orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));
        Set<Trainer> currentTrainer = trainee.getTrainers();
        List<Trainer> trainers = assignTrainersRequestDto.getTrainerUsernameList().stream()
                .map(trainerUsername -> trainerRepository.findByUserUsername(trainerUsername)
                        .orElseThrow(() -> new TrainerException(Constants.TRAINER_NOT_FOUND))).toList();

        currentTrainer.stream().forEach(trainer -> trainer.getTrainees().remove(trainee));
        trainers.forEach(trainer -> trainer.getTrainees().add(trainee));
        trainee.setTrainers(new HashSet<>(trainers));
        traineeRepository.save(trainee);
        List<TrainerResponseDto> trainerResponseDtoList = trainers.stream().map(ObjectConvertor::trainerToTrainerDto).toList();
        log.info("{}:: assignTrainers {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return trainerResponseDtoList;
    }

    @Transactional
    public void deleteTrainee(String username)
    {
        log.info("{}:: deleteTrainee {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserException(Constants.USER_NOT_FOUND));
        Trainee trainee = traineeRepository.findTraineeByUser(user)
                .orElseThrow(() -> new TraineeException(Constants.TRAINEE_NOT_FOUND));

        trainee.getTrainers().stream().forEach(trainer -> trainer.removeTrainee(trainee));
       
        trainee.getTrainers().clear();
        log.info("{}:: deleteTrainee {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        traineeRepository.delete(trainee);
    }

}
