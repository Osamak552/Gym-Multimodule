package com.epam.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TrainingTypeResponseDto {
    private long trainingTypeId;
    private String trainingTypeName;
}
