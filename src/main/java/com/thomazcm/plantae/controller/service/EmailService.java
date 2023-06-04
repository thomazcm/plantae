package com.thomazcm.plantae.controller.service;

import java.io.ByteArrayOutputStream;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.thomazcm.plantae.model.Ingresso;

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

	public void sendPdfEmail(Ingresso ingresso, ByteArrayOutputStream pdf, String nomeIngresso) {
		try {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        
	        // Set the recipient email address
	        helper.setTo(ingresso.getEmail());
	        
	        //
	        helper.setFrom("☘ Plantae - Cozinha Vegetal ☘<contato.plantaecozinhavegetal@gmail.com>");
	        
	        // Set the email subject
	        helper.setSubject("Ingresso Brunch Plantae - " + ingresso.getCliente());
	        
	        // Set the email body
	        helper.setText(getHtmlBody(), true);
	        
	        // Attach the PDF to the email
	        helper.addAttachment("ingresso-plantae", new ByteArrayDataSource(pdf.toByteArray(), "application/pdf"));
	        
	        // Send the email
	        javaMailSender.send(message);
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	private String getHtmlBody() {
		return "<!DOCTYPE html>\r\n"
		        + "<html>\r\n"
		        + "<head>\r\n"
		        + "    <meta charset=\"UTF-8\">\r\n"
		        + "    <title>Brunch Plantae - 1ª Edição</title>\r\n"
		        + "    <style>\r\n"
		        + "        body {\r\n"
		        + "            font-family: Arial, sans-serif;\r\n"
		        + "            background-color: #f7f7f7;\r\n"
		        + "            color: #333333;\r\n"
		        + "        }\r\n"
		        + "        h1 {\r\n"
		        + "            color: #009688;\r\n"
		        + "        }\r\n"
		        + "        p {\r\n"
		        + "            margin-bottom: 16px;\r\n"
		        + "        }\r\n"
		        + "        .emoji {\r\n"
		        + "            font-size: 20px;\r\n"
		        + "            margin-right: 5px;\r\n"
		        + "        }\r\n"
		        + "        .signature {\r\n"
		        + "            font-style: italic;\r\n"
		        + "        }\r\n"
		        + "    </style>\r\n"
		        + "</head>\r\n"
		        + "<body>\r\n"
		        + "    <h1>Brunch Plantae - 1ª Edição</h1>\r\n"
		        + "    <p>Obrigada por fazer parte do primeiro Brunch Plantae - Cozinha Vegetal!</p>\r\n"
		        + "    <p>Envio seu ingresso em anexo. Mal podemos esperar pra encontrar você no dia 2!</p>\r\n"
		        + "    <p class=\"emoji\">🌱🌿🥕🥑</p>\r\n"
		        + "    <p class=\"signature\">Beijos Veganos,</p>\r\n"
		        + "    <p class=\"signature\">Laura e Giulia - Plantae 🌱</p>\r\n"
		        + "</body>\r\n"
		        + "</html>\r\n";
	}
}