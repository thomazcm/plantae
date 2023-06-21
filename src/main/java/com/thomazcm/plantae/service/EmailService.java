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


    private String getHtmlBody(String emailTemplate) {
        return "<!DOCTYPE html>\r\n"
                + "<html lang=\"pt-br\">\r\n"
                + "\r\n"
                + "<head>\r\n"
                + "    <meta charset=\"UTF-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
                + "    <style>\r\n"
                + "        @import url('https://fonts.googleapis.com/css2?family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');\r\n"
                + "        @import url('https://fonts.googleapis.com/css2?family=Nunito:wght@400;700&display=swap');\r\n"
                + "\r\n"
                + "        body {\r\n"
                + "            font-family: 'Nunito', sans-serif;\r\n"
                + "            background-color: #f7e7d4;\r\n"
                + "            margin: 0;\r\n"
                + "            padding: 0;\r\n"
                + "            color: #424242;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        .container {\r\n"
                + "            max-width: 600px;\r\n"
                + "            margin: 50px auto;\r\n"
                + "            padding: 20px;\r\n"
                + "            background-color: #ffffff;\r\n"
                + "            border-radius: 10px;\r\n"
                + "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);\r\n"
                + "            text-align: center;\r\n"
                + "            border: 1px solid #e7e7e7;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        h1 {\r\n"
                + "            font-family: 'Libre Baskerville', serif;\r\n"
                + "            font-weight: 700;\r\n"
                + "            color: #84312e;\r\n"
                + "            font-size: 25px;\r\n"
                + "            margin-bottom: 30px;\r\n"
                + "            text-transform: uppercase;\r\n"
                + "            letter-spacing: 2px;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        p {\r\n"
                + "            font-size: 16px;\r\n"
                + "            line-height: 1.8;\r\n"
                + "            margin-bottom: 20px;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        .signature {\r\n"
                + "            margin-top: 30px;\r\n"
                + "            font-style: italic;\r\n"
                + "            font-size: 18px;\r\n"
                + "            line-height: 1.5;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        .color-text {\r\n"
                + "            color: #84312e;\r\n"
                + "            font-weight: 700;\r\n"
                + "            font-size: 20px;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        .survey-link {\r\n"
                + "            font-size: 24px;\r\n"
                + "            color: #388e3c;\r\n"
                + "            text-decoration: underline;\r\n"
                + "            font-weight: bold;\r\n"
                + "        }\r\n"
                + "\r\n"
                + "        .center-image {\r\n"
                + "            display: block;\r\n"
                + "            margin-left: auto;\r\n"
                + "            margin-right: auto;\r\n"
                + "            width: 50%;\r\n"
                + "            height: auto;\r\n"
                + "            min-width: 320px;\r\n"
                + "        }\r\n"
                + "    </style>\r\n"
                + "</head>\r\n"
                + "\r\n"
                + "<body>\r\n"
                + "    <div class=\"container\" style=\"margin-bottom: 25px\">\r\n"
                + "        <div>\r\n"
                + "            <h1>A sua opinião é muito importante<br> para nós!</h1>\r\n"
                + "            <p>Estamos na contagem regressiva para o nosso 1º Brunch Vegetal da Plantae e não poderíamos estar mais\r\n"
                + "                empolgadas!</p>\r\n"
                + "            <p>Gostaríamos de fazer o seu dia conosco o mais agradável e inesquecível possível.<br> Por isso, <b>criamos\r\n"
                + "                    um pequeno questionário com 5 perguntinhas</b> para entender melhor as suas preferências.</p>\r\n"
                + "            <p>Seu tempo é valioso então não deve levar mais do que dois minutinhos: <br><a\r\n"
                + "                    href=\"https://docs.google.com/forms/d/e/1FAIpQLScXuvnzZuNGJRpiLK6sgp_WyiEkbVnVNp4HbxqOVERu5UhO6A/viewform?pli=1\"\r\n"
                + "                    class=\"survey-link\">RESPONDER AGORA</a></p>\r\n"
                + "        </div>\r\n"
                + "        <a  href=\"https://docs.google.com/forms/d/e/1FAIpQLScXuvnzZuNGJRpiLK6sgp_WyiEkbVnVNp4HbxqOVERu5UhO6A/viewform?pli=1\">\r\n"
                + "            <img \r\n"
                + "            src=\"https://raw.githubusercontent.com/thomazcm/plantae/master/src/main/resources/static/png/survey.png\"\r\n"
                + "            class=\"center-image\" alt=\"Girls holding survey papers\" >\r\n"
                + "        </a>\r\n"
                + "        <div>\r\n"
                + "            <p>Sua resposta vai nos ajudar a tornar o evento ainda mais especial!</p>\r\n"
                + "            <p>Contamos com a sua colaboração e mal podemos esperar para te ver no dia 2 de julho!</p>\r\n"
                + "            <div class=\"signature\">\r\n"
                + "                <p><span style=\"color:#388e3c\">Beijos verdes,</span></p>\r\n"
                + "                <p style=\"color:#388e3c\">Laura e Giulia</p>\r\n"
                + "            </div>\r\n"
                + "        </div>\r\n"
                + "    </div>\r\n"
                + "</body>\r\n"
                + "\r\n"
                + "</html>";
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

//    private String getHtmlBody(String emailTemplate) {
//        Resource resource = new ClassPathResource("templates/email/" + emailTemplate + ".html");
//        try (InputStream inputStream = resource.getInputStream();
//                var scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
//
//            return scanner.useDelimiter("\\A").next();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("Error accessing the HTML email template");
//            return null;
//        }
//    }
}
