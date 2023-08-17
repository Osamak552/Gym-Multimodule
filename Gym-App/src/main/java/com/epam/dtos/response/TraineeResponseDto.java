package com.epam.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TraineeResponseDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
