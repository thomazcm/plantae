package com.thomazcm.plantae.controller.service;

import java.io.ByteArrayOutputStream;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

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
	        
	        // Set the recipient email address
	        helper.setTo(to);
	        
	        // Set the sender
	        helper.setFrom("☘ Plantae - Cozinha Vegetal ☘<contato.plantaecozinhavegetal@gmail.com>");
	        
	        // Set the email subject
	        helper.setSubject("Ingresso Brunch Plantae - " + nomeCliente);
	        
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
				+ "<html lang=\"en\">\r\n"
				+ "<head>\r\n"
				+ "  <meta charset=\"UTF-8\">\r\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "  <style>\r\n"
				+ "    /* CSS styles */\r\n"
				+ "    body {\r\n"
				+ "      font-family: Arial, sans-serif;\r\n"
				+ "      background-color: #f5f5f5;\r\n"
				+ "      margin: 0;\r\n"
				+ "      padding: 0;\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    .container {\r\n"
				+ "      max-width: 600px;\r\n"
				+ "      margin: 0 auto;\r\n"
				+ "      padding: 20px;\r\n"
				+ "      background-color: #ffffff;\r\n"
				+ "      border-radius: 10px;\r\n"
				+ "      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    h1 {\r\n"
				+ "      color: #84312e;\r\n"
				+ "      font-size: 28px;\r\n"
				+ "      margin-bottom: 30px;\r\n"
				+ "      text-align: center;\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    p {\r\n"
				+ "      font-size: 18px;\r\n"
				+ "      line-height: 1.5;\r\n"
				+ "      margin-bottom: 15px;\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    .emoji {\r\n"
				+ "      font-size: 24px;\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    .signature {\r\n"
				+ "      margin-top: 40px;\r\n"
				+ "      text-align: center;\r\n"
				+ "    }\r\n"
				+ "\r\n"
				+ "    .green-text {\r\n"
				+ "      color: #4caf50;\r\n"
				+ "      font-weight: bold;\r\n"
				+ "    }\r\n"
				+ "  </style>\r\n"
				+ "</head>\r\n"
				+ "<body>\r\n"
				+ "  <div class=\"container\">\r\n"
				+ "    <h1>Oba! Seu convite para o 1º Brunch Vegetal da Plantae já está disponível!</h1>\r\n"
				+ "    <p class=\"emoji\">&#127813; &#127807;</p>\r\n"
				+ "    <p>Estamos extremamente contentes por ter você com a gente neste momento tão especial!</p>\r\n"
				+ "    <p>Desejamos que este dia seja preenchido com experiências maravilhosas e momentos deliciosos.</p>\r\n"
				+ "    <p>Agradecemos pela confiança no nosso trabalho e esperamos que você desfrute de tudo o que foi preparado com tanto carinho e dedicação.</p>\r\n"
				+ "    <p class=\"emoji\">&#127797; &#127815;</p>\r\n"
				+ "    <p>Nos vemos dia 2 de julho! Até lá!</p>\r\n"
				+ "    <div class=\"signature\">\r\n"
				+ "      <p><span class=\"green-text\">Beijos verdes</span>,</p>\r\n"
				+ "      <p>Laura e Giulia</p>\r\n"
				+ "    </div>\r\n"
				+ "  </div>\r\n"
				+ "</body>\r\n"
				+ "</html>\r\n"
				+ "";
	}
}