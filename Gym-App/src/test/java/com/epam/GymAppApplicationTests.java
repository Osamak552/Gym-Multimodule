package com.epam;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class})
class GymAppApplicationTests {
//    @Test
//    void contextLoads() {
//    }
}


//clean verify sonar:sonar -Dsonar.projectKey=CICDTASK -Dsonar.projectName='CICDTASK' -Dsonar.host.url=http://localhost:9000-Dsonar.token=sqp_35d913a68d552ec15d0ea248e124ae3d0e871598
