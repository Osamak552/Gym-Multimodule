package com.epam.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerProfileDto {
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeResponseDto> traineeResponseDtoList;
}
