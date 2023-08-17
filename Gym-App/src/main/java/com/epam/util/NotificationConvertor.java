package com.epam.util;


import com.epam.dtos.request.NotificationDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;


import java.util.HashMap;
import java.util.Map;

public class NotificationConvertor {
    private NotificationConvertor(){}

    public static NotificationDto getNotificationDto(String username,String password){
        Map<String,String> emailInfo = new HashMap<>();
        emailInfo.put(Constants.USERNAME,username);
        emailInfo.put("password",password);
        return NotificationDto.builder()
                .emailInfo(emailInfo)
                .to(username)
                .emailType("Registration")
                .build();

    }

    public static NotificationDto getNotificationDto(Trainer trainerUpdate){
        Map<String,String> emailInfo = new HashMap<>();
        emailInfo.put(Constants.USERNAME,trainerUpdate.getUser().getUsername());
        emailInfo.put("firstName",trainerUpdate.getUser().getFirstName());
        emailInfo.put("lastName",trainerUpdate.getUser().getLastName());
        emailInfo.put("specialization",trainerUpdate.getSpecialization().getTrainingTypeName());
        return NotificationDto.builder()
                .emailInfo(emailInfo)
                .to(trainerUpdate.getUser().getEmail())
                .emailType("Trainer_Update")
                .build();
    }

    public static NotificationDto getNotificationDto(Trainee traineeUpdate){
        Map<String,String> emailInfo = new HashMap<>();
        emailInfo.put(Constants.USERNAME,traineeUpdate.getUser().getUsername());
        emailInfo.put("firstName",traineeUpdate.getUser().getFirstName());
        emailInfo.put("lastName",traineeUpdate.getUser().getLastName());
        emailInfo.put("dateOfBirth",traineeUpdate.getDateOfBirth().toString());
        emailInfo.put("address",traineeUpdate.getAddress());
        return NotificationDto.builder()
                .emailInfo(emailInfo)
                .to(traineeUpdate.getUser().getEmail())
                .emailType("Trainee_Update")
                .build();

    }

    public static NotificationDto getNotificationDto(Training training){
        Map<String,String> emailInfo = new HashMap<>();
        emailInfo.put("traineeUsername",training.getTrainee().getUser().getUsername());
        emailInfo.put("trainerUsername",training.getTrainer().getUser().getUsername());
        emailInfo.put("trainingName",training.getTrainingName());
        emailInfo.put("trainingType",training.getTrainingType().getTrainingTypeName());
        emailInfo.put("date",training.getTrainingDate().toString());
        emailInfo.put("duration",String.valueOf(training.getTrainingDuration()));
        return NotificationDto.builder()
                .emailInfo(emailInfo)
                .to(training.getTrainee().getUser().getEmail()+","
                +training.getTrainer().getUser().getEmail())
                .emailType("Training_Created")
                .build();
    }

}
