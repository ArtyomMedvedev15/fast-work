package io.project.fastwork.services.api;

public interface MailServiceApi {
    boolean sendMail(String emailTo,String subject, String message);
}
