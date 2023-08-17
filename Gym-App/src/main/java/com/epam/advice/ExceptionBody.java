package com.epam.advice;

import lombok.*;

import java.util.Map;

@Getter

@Setter

@AllArgsConstructor

@NoArgsConstructor

@ToString
public class ExceptionBody {
    private String time;
    private String path;
    private String status;
    private Map<String,String> errorMap;

}
