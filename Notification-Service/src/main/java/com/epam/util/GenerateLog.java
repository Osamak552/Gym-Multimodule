package com.epam.util;

import com.epam.collections.EmailLog;
import org.springframework.mail.SimpleMailMessage;
import java.util.Arrays;
import java.util.Date;

public class GenerateLog {
    private GenerateLog(){};
    public static EmailLog generateEmailLog(SimpleMailMessage simpleMailMessage,boolean status){
        return EmailLog.builder()
                .to(Arrays.asList(simpleMailMessage.getTo()))
                .from("usamak552@gmail.com")
                .status(status)
                .subject(simpleMailMessage.getSubject())
                .body(simpleMailMessage.getText())
                .date(new Date())
                .build();
    }
}
