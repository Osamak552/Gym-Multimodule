package com.epam.dtos.request;

import lombok.Data;

import java.util.List;

@Data
public class AssignTrainersRequestDto {
    private String traineeUsername;
    private List<String> trainerUsernameList;

}
