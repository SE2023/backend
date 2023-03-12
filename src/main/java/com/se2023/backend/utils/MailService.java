package com.se2023.backend.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;

@Service
public class MailService {
    @Autowired
    private JavaMailSenderImpl mailSender;
//    @Value("$(spring.mail.username)")
//    private String from;

    public void sendEmail(String to,String subject, String text){
        SimpleMailMessage message =new SimpleMailMessage();
        //message.setFrom(from);
        message.setFrom("jiayixxx@126.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
