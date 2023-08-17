package com.epam.service.implementation;



import com.epam.collections.EmailLog;
import com.epam.repositories.EmailLogRepository;
import com.epam.service.EmailNotificationService;
import com.epam.util.GenerateLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
;

@Service
@Slf4j
public class EmailNotificationServiceImpl implements EmailNotificationService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailLogRepository emailLogRepository;
    public void sendEmailNotification(SimpleMailMessage simpleMailMessage)
    {
        log.info("{}::sendEmailNotification execution started",this.getClass().getName());
        javaMailSender.send(simpleMailMessage);
        EmailLog emailLog = GenerateLog.generateEmailLog(simpleMailMessage,true);
        emailLogRepository.save(emailLog);
        log.info("{}::sendEmailNotification execution ended",this.getClass().getName());
    }


}
