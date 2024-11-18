package com.hust.project3.services;

import com.hust.project3.entities.EmailDetails;
import jakarta.mail.MessagingException;
import org.thymeleaf.context.Context;

public interface EmailService {
    void sendSimpleMail(EmailDetails details);
    void sendMailWithAttachment(EmailDetails details) throws MessagingException;
    void sendEmailWithHtmlTemplate(String recipient, String subject, String templateName, Context context) throws MessagingException;
}
