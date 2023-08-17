package com.epam.dtos.request;

import com.epam.validator.DifferentPasswords;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@DifferentPasswords
public class ChangePasswordDto {
    @NotBlank(message = "Username cannot be empty")
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "New password cannot be empty")
    private String newPassword;
}
