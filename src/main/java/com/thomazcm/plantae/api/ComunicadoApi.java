package com.thomazcm.plantae.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.model.config.UserConfiguration;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.repository.IngressoRepository;
import com.thomazcm.plantae.service.EmailService;

@RestController
@RequestMapping("/comunicado")
public class ComunicadoApi {

    private final IngressoRepository ingressoRepo;
    private final ConfigurationRepository configRepo;
    private final EmailService emailService;

    public ComunicadoApi(IngressoRepository ingressoRepo, ConfigurationRepository configRepo,
            EmailService emailService) {
        this.ingressoRepo = ingressoRepo;
        this.configRepo = configRepo;
        this.emailService = emailService;
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        return ResponseEntity.ok(getAllCustomerEmails());
    }

    @PostMapping("/config")
    public ResponseEntity<String> setConfig(@RequestBody Configurations configurations) {
        UserConfiguration config = configRepo.getConfig();

        configurations.getValues().forEach((key, value) -> {
            config.getTextConfigurations().put(key, value);
        });

        configRepo.save(config);
        return ResponseEntity.ok("saved");
    }


    @GetMapping("/send/{index}")
    public ResponseEntity<?> sendEmails(@PathVariable int index) {
        UserConfiguration config = configRepo.getConfig();
        HashMap<String, String> configurations = config.getTextConfigurations();
        var allEmails = getAllCustomerEmails();
        String surveyBody = configurations.get("survey-body");


        Boolean emailSent = configurations.get("email-sent") != null
                ? Boolean.parseBoolean(configurations.get("email-sent"))
                : true;
        if (emailSent) {
            return ResponseEntity.ok("email already sent");
        }



        List<String> customerEmails = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            customerEmails.add(allEmails.get(index));
            index++;
        }

        customerEmails.parallelStream().forEach(email -> {
            emailService.sendEmailTemplate(email, surveyBody, "surveyEmailTemplate");
            System.out.println("sent email to " + email);
        });

        configurations.put("email-sent", "true");
        configRepo.save(config);
        return ResponseEntity.ok("sent!");
    }

    @GetMapping("/send/test")
    public ResponseEntity<?> sendEmailsTest() {
        UserConfiguration config = configRepo.getConfig();
        HashMap<String, String> configurations = config.getTextConfigurations();
        String surveyBody = configurations.get("survey-body");


        Boolean emailSent = configurations.get("email-sent") != null
                ? Boolean.parseBoolean(configurations.get("email-sent"))
                : true;
        if (emailSent) {
            return ResponseEntity.ok("email already sent");
        }

        emailService.sendEmailTemplate("thomazcm@gmail.com", surveyBody, "surveyEmailTemplate");

        configurations.put("email-sent", "true");
        configRepo.save(config);
        return ResponseEntity.ok("sent!");
    }


    private List<String> getAllCustomerEmails() {
        List<String> emails = ingressoRepo.findAll().stream().map(Ingresso::getEmail)
                .map(String::toLowerCase).distinct().toList();
        return emails;
    }



}
