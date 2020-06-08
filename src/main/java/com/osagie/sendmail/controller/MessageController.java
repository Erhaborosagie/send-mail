package com.osagie.sendmail.controller;

import com.osagie.sendmail.config.EmailConfig;
import com.osagie.sendmail.domain.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;


@RestController
@Slf4j
public class MessageController {
    private EmailConfig emailConfig;

    public MessageController(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }

    @PostMapping("/sendmessage")
    public void sendMessage(@RequestBody ResponseMessage message,
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("Message is not valid");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(message.getEmail());
        mailMessage.setFrom("ab@bc.cd");
        mailMessage.setSubject("New message for " + message.getName());
        mailMessage.setText(message.getMessage());

        mailSender.send(mailMessage);

    }
}
