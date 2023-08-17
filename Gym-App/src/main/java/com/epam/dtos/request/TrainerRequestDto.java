package com.epam.dtos.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainerRequestDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @Email(message = "please provide proper email")
    private String email;
    @NotBlank(message = "Specialization cannot be empty")
    private String specialization;
}
