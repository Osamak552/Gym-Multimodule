package com.epam.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainerUpdateRequestDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @NotBlank(message = "Specialization cannot be empty")
    private String specialization;
    private boolean isActive;
}
