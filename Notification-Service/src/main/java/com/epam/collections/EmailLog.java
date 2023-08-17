package com.epam.collections;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "email-logs")
@Builder
public class EmailLog {
    private String from;
    private List<String> to;
    private String subject;
    private String body;
    private boolean status;
    private Date date;
}
