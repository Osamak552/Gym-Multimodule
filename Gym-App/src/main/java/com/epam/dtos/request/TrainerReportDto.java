package com.epam.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class TrainerReportDto {
    @NotBlank(message = "Username cannot be empty")
    private String trainerUsername;
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    private boolean isActive;
    @NotNull(message = "Training date must not be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainingDate;
    @Min(value = 15, message = "Duration must be at least 15")
    @Max(value = 100, message = "Duration must be at most 180")
    private int trainingDuration;
}
