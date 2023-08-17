package com.epam.dtos.request;


import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
public class TrainingRequestDto {
    @NotBlank(message = "Trainee username cannot be empty")
    private String traineeUsername;
    @NotBlank(message = "Trainer username cannot be empty")
    private String trainerUsername;
    @NotBlank(message = "Training name cannot be empty")
    private String trainingName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate trainingDate;
    @Min(value = 15, message = "Duration must be at least 15")
    @Max(value = 100, message = "Duration must be at most 180")
    private int trainingDuration;
}
