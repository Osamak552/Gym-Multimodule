package com.epam.dtos.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Data
public class TraineeRequestDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @Email(message = "please provide proper email")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Please provide the proper date of birth")
    private LocalDate dateOfBirth;
    private String address;
}
