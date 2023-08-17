package com.epam.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTypeRequestDto {
    @NotBlank(message = "Training type cannot be empty")
    private String trainingTypeName;
}
