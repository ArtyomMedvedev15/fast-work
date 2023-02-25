package io.project.fastwork.services.implementation;

import io.project.fastwork.services.api.MailServiceApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
@Service
public class MailServiceApiImpl implements MailServiceApi {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String username;

    @Override
    public boolean sendMail(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        try {
            log.info("Mail to {} send in {}", emailTo, new Date());
            javaMailSender.send(mailMessage);
            return true;
        }catch (MailException mailException){
            log.error("Mail cannot send, throw exception with {} in {}",mailException.getMessage(),new Date());
            return false;
        }
    }
}
