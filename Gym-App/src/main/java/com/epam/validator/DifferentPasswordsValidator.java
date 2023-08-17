package com.epam.validator;

import com.epam.dtos.request.ChangePasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class DifferentPasswordsValidator implements ConstraintValidator<DifferentPasswords, ChangePasswordDto> {
    @Override
    public boolean isValid(ChangePasswordDto dto, ConstraintValidatorContext context) {
        return !dto.getPassword().equals(dto.getNewPassword());
    }
}