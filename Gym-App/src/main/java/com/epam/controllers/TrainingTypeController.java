package com.epam.controllers;


import com.epam.dtos.request.TrainingTypeRequestDto;
import com.epam.dtos.response.TrainingTypeResponseDto;
import com.epam.services.TrainingTypeService;
import com.epam.util.Constants;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home/training-type")
@Slf4j
public class TrainingTypeController {

    @Autowired
    private TrainingTypeService trainingTypeService;

    @PostMapping("")
    public ResponseEntity<TrainingTypeResponseDto> createTrainingType(@RequestBody @Valid TrainingTypeRequestDto trainingTypeRequestDto)
    {
        log.info("{}:: createTrainingType {}",this.getClass().getName(), Constants.EXECUTION_STARTED);
        TrainingTypeResponseDto trainingTypeResponseDto = trainingTypeService.createTrainingType(trainingTypeRequestDto);
        log.info("{}:: createTrainingType {}",this.getClass().getName(), Constants.EXECUTION_ENDED);
        return new ResponseEntity<>(trainingTypeResponseDto, HttpStatus.CREATED);
    }
}
