package com.epam;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//password
//c21fdd565bdf43758193cd54c6388ec2
@SpringBootApplication
public class GymAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymAppApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
