package com.epam.dtos.request;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
public class NotificationDto {
    private Map<String,String>  emailInfo;
    private String to;
    private String emailType;
}

