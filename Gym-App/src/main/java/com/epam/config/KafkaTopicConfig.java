package com.epam.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.notification}")
    private String notificationTopic;
    @Value("${spring.kafka.topic.report}")
    private String reportTopic;

    @Bean
    public NewTopic emailNotificationTopic(){
        return TopicBuilder.name(notificationTopic)
                .build();
    }

    @Bean
    public NewTopic reportTopic(){
        return TopicBuilder.name(reportTopic)
                .build();
    }
}
