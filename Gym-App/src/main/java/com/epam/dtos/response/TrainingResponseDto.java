package com.epam.dtos.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainingResponseDto {
    private String trainingName;
    private String trainingType;
    private String trainerName;
    private String traineeName;
    private int trainingDuration;
    private LocalDate trainingDate;
}
