package com.epam.service.implementation;

import com.epam.dtos.request.NotificationDto;
import com.epam.service.EmailIGenerationService;
import com.epam.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailIGenerationServiceImpl implements EmailIGenerationService {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    public void registrationEmailInfo(NotificationDto notificationDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("mohammadosama.21910331@viit.ac.in");
        simpleMailMessage.setSubject("Registration Successfully!!!");
        simpleMailMessage.setText("Hello Dear, \n\nYour account has been created\nYour Credentials-\nUsername: " + notificationDto.getEmailInfo().get("username") + "\n"
                + "Password: " + notificationDto.getEmailInfo().get("password") + "\n\nThanks and regards!");
        emailNotificationService.sendEmailNotification(simpleMailMessage);
    }

    @Override
    public void traineeUpdateEmailInfo(NotificationDto notificationDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("mohammadosama.21910331@viit.ac.in");
        simpleMailMessage.setSubject("Trainee Updated");
        simpleMailMessage.setText("Hello Dear, \n\nYour account has been updated\nYour Details -\nUsername: " + notificationDto.getEmailInfo().get("username") +
                "\nName: " + notificationDto.getEmailInfo().get("firstName") + " " + notificationDto.getEmailInfo().get("lastName") +
                "\nDate of birth: " + notificationDto.getEmailInfo().get("dateOfBirth") +
                "\nAddress: " + notificationDto.getEmailInfo().get("address") +
                "\n\nThanks and regards!");
        emailNotificationService.sendEmailNotification(simpleMailMessage);
    }

    @Override
    public void trainerUpdateEmailInfo(NotificationDto notificationDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("mohammadosama.21910331@viit.ac.in");
        simpleMailMessage.setSubject("Trainer Updated");
        simpleMailMessage.setText("Hello Dear, \n\nYour account has been updated\nYour Details -\nUsername: " + notificationDto.getEmailInfo().get("username") +
                "\nName: " + notificationDto.getEmailInfo().get("firstName") + " " + notificationDto.getEmailInfo().get("lastName") +
                "\nSpecialization: " + notificationDto.getEmailInfo().get("specialization") +
                "\n\nThanks and regards!");
        emailNotificationService.sendEmailNotification(simpleMailMessage);

    }

    @Override
    public void trainingEmailInfo(NotificationDto notificationDto) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo("mohammadosama.21910331@viit.ac.in");
        simpleMailMessage.setSubject("Training is Scheduled");
        simpleMailMessage.setText("Hello Dear, \n\nYour training has been scheduled\nYour training Details -\nTrainer: " + notificationDto.getEmailInfo().get("trainerUsername") +
                "\nTraining name: " + notificationDto.getEmailInfo().get("trainingName") +
                "\nTrainee: " + notificationDto.getEmailInfo().get("traineeUsername") +
                "\nType: " + notificationDto.getEmailInfo().get("trainingType") +
                "\nDate: " + notificationDto.getEmailInfo().get("date") +
                "\nDuration: " + notificationDto.getEmailInfo().get("duration") +
                "\nThanks and regards"
        );
        emailNotificationService.sendEmailNotification(simpleMailMessage);
    }
}
