package com.thomazcm.plantae.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import javax.mail.MessagingException;
import javax.mail.util.ByteArrayDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.thomazcm.plantae.config.properties.MailProperties;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public EmailService(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.mailProperties = mailProperties;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(mailProperties.getSender());

        javaMailSender.send(message);
    }

    public void sendEmailTemplate(String emailCliente, String subject, String emailTemplate) {
        try {
            var message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setTo(emailCliente);
            messageHelper.setFrom(mailProperties.getSender());
            messageHelper.setSubject(subject);
            messageHelper.setText(getHtmlBody(emailTemplate), true);
            javaMailSender.send(message);

        } catch (MessagingException e) {
            System.out.println("Failed to send email template");
            e.printStackTrace();
        }
    }


    public void sendPdfEmail(String emailCliente, ByteArrayOutputStream pdf, String nomeIngresso,
            String nomeCliente, String emailTemplate) {
        try {
            var message = javaMailSender.createMimeMessage();
            var messageHelper = new MimeMessageHelper(message, true);

            messageHelper.setTo(emailCliente);
            messageHelper.setFrom(mailProperties.getSender());
            messageHelper.setSubject(mailProperties.getSubject() + nomeCliente);
            messageHelper.setText(getHtmlBody(emailTemplate), true);

            messageHelper.addAttachment(nomeIngresso,
                    new ByteArrayDataSource(pdf.toByteArray(), "application/pdf"));

            javaMailSender.send(message);

        } catch (MessagingException e) {
            System.out.println("Failed to send ticket email");
            e.printStackTrace();
        }
    }

    private String getHtmlBody(String emailTemplate) {
        Resource resource = new ClassPathResource("templates/email/" + emailTemplate + ".html");
        try (InputStream inputStream = resource.getInputStream();
                var scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {

            return scanner.useDelimiter("\\A").next();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error accessing the HTML email template");
            return null;
        }
    }
}
