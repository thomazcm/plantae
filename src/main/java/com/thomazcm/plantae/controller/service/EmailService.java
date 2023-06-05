package com.thomazcm.plantae.controller.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

	public void sendPdfEmail(String to, ByteArrayOutputStream pdf, String nomeIngresso, String nomeCliente) {
		try {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        
	        helper.setTo(to);
	        
	        helper.setFrom("Plantae - Cozinha Vegetal <contato.plantaecozinhavegetal@gmail.com>");
	        
	        helper.setSubject("Ingresso Brunch Plantae - " + nomeCliente);
	        
	        helper.setText(getHtmlBody(), true);
	        
	        helper.addAttachment(nomeIngresso, new ByteArrayDataSource(pdf.toByteArray(), "application/pdf"));
	        
	        javaMailSender.send(message);
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	private String getHtmlBody() {
		Resource resource = new ClassPathResource("templates/emailTemplate.html");
		try {
			return new String(Files.readAllBytes(resource.getFile().toPath()));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}