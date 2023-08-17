package com.epam.controllers;

import com.epam.dtos.request.AssignTrainersRequestDto;
import com.epam.dtos.request.TraineeRequestDto;
import com.epam.dtos.request.TraineeUpdateRequestDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeResponseDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.services.TraineeService;
import com.epam.util.Constants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/home/trainee")
@Slf4j
public class TraineeController {
    @Autowired
    private TraineeService traineeService;

    @PostMapping("/register")
    public ResponseEntity<TraineeResponseDto> createTrainer(@RequestBody @Valid TraineeRequestDto traineeRequestDto)
    {
        log.info("{}:: createTrainer {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TraineeResponseDto traineeResponseDto = traineeService.createTrainee(traineeRequestDto);
        log.info("{}:: createTrainer {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(traineeResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> getTraineeProfile( @PathVariable String username)
    {
        log.info("{}:: getTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TraineeProfileDto traineeProfileDto = traineeService.getTraineeProfile(username);
        log.info("{}:: getTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(traineeProfileDto,HttpStatus.OK);
    }


    @PutMapping("/{username}")
    public ResponseEntity<TraineeProfileDto> updateTraineeProfile( @PathVariable String username, @RequestBody @Valid TraineeUpdateRequestDto traineeUpdateRequestDto)
    {
        log.info("{}:: updateTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TraineeProfileDto traineeProfileDto = traineeService.updateTraineeProfile(username,traineeUpdateRequestDto);
        log.info("{}:: updateTraineeProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(traineeProfileDto,HttpStatus.OK);
    }

    @PutMapping("/assign-trainers")
    public ResponseEntity<List<TrainerResponseDto>> assignTrainers(@RequestBody @Valid AssignTrainersRequestDto assignTrainersRequestDto)
    {
        log.info("{}:: assignTrainers {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        List<TrainerResponseDto> trainerResponseDtoList = traineeService.assignTrainers(assignTrainersRequestDto);
        log.info("{}:: assignTrainers {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainerResponseDtoList,HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTrainee(@PathVariable  String username)
    {
        log.info("{}:: deleteTrainee {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        traineeService.deleteTrainee(username);
        log.info("{}:: deleteTrainee {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
    }

}
