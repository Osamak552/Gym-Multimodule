package com.epam.controllers;

import com.epam.dtos.request.TrainerRequestDto;
import com.epam.dtos.request.TrainerUpdateRequestDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerResponseDto;
import com.epam.services.TrainerService;
import com.epam.util.Constants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/home/trainer")
@Slf4j
public class TrainerController {

    @Autowired
    private TrainerService trainerService;

    @PostMapping("/register")
    public ResponseEntity<TrainerResponseDto> createTrainer(@RequestBody @Valid TrainerRequestDto trainerRequestDto)
    {
        log.info("{}:: createTrainer {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TrainerResponseDto trainerResponseDto = trainerService.createTrainer(trainerRequestDto);
        log.info("{}:: createTrainer {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainerResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileDto> getTrainerProfile(@PathVariable String username)
    {
        log.info("{}:: getTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TrainerProfileDto trainerProfileDto = trainerService.getTrainerProfile(username);
        log.info("{}:: getTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainerProfileDto,HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileDto> updateTrainerProfile(@PathVariable String username,@RequestBody @Valid TrainerUpdateRequestDto trainerUpdateRequestDto)
    {
        log.info("{}:: updateTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TrainerProfileDto trainerProfileDto = trainerService.updateTrainerProfile(username,trainerUpdateRequestDto);
        log.info("{}:: updateTrainerProfile {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainerProfileDto,HttpStatus.OK);
    }

    @GetMapping("/unassigned-trainer/{traineeUsername}")
    public ResponseEntity<List<TrainerResponseDto>> getUnassignedTrainerFromTrainee(@PathVariable String traineeUsername)
    {
        log.info("{}:: getUnassignedTrainerFromTrainee {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        List<TrainerResponseDto> trainerResponseDtoList = trainerService.getUnassignedTrainersFromTrainee(traineeUsername);
        log.info("{}:: getUnassignedTrainerFromTrainee {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainerResponseDtoList,HttpStatus.OK);
    }
}
