package com.epam.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Map<String,String>  emailInfo;
    private String to;
    private String emailType;
}

