package com.epam.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;



@Data
public class TraineeUpdateRequestDto {
    @NotBlank(message = "First name cannot be empty")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Please provide the proper date of birth")
    @NotNull(message = "Date of birth must not be null")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    private boolean isActive;
}
