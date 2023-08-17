package com.epam.controllers;

import com.epam.dtos.request.TraineeTrainingsDto;
import com.epam.dtos.request.TrainerTrainingsDto;
import com.epam.dtos.request.TrainingRequestDto;
import com.epam.dtos.response.TrainingResponseDto;
import com.epam.services.TrainingService;
import com.epam.util.Constants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home/training")
@Slf4j
public class TrainingController {

    @Autowired
    private TrainingService trainingService;

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.OK)
    public void createTraining(@RequestBody @Valid TrainingRequestDto trainingRequestDto)
    {
        log.info("{}:: createTraining {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        trainingService.createTraining(trainingRequestDto);
        log.info("{}:: createTraining {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
    }
    @GetMapping("/Trainee-TrainingList")
    public ResponseEntity<List<TrainingResponseDto>> getTraineeTrainings(TraineeTrainingsDto traineeTrainingsDto)
    {
        log.info("{}:: getTraineeTrainings {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        List<TrainingResponseDto> trainings = trainingService.getTraineeTrainings(traineeTrainingsDto);
        log.info("{}:: getTraineeTrainings {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainings,HttpStatus.OK);
    }

    @GetMapping("/Trainer-TrainingList")
    public ResponseEntity<List<TrainingResponseDto>> getTrainerTrainings(TrainerTrainingsDto trainerTrainingsDto)
    {
        log.info("{}:: getTrainerTrainings {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        List<TrainingResponseDto> trainings = trainingService.getTrainerTrainings(trainerTrainingsDto);
        log.info("{}:: getTrainerTrainings {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainings,HttpStatus.OK);
    }
}
