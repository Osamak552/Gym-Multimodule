package com.epam.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


@Data
@Builder
public class TraineeTrainingsDto {
    @NotBlank(message = "Username cannot be empty")
    private String traineeUsername;
    private String trainerUsername;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate periodTo;
    private String trainingType;
}
