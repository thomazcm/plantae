package com.thomazcm.plantae.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.api.service.EmailApiService;
import com.thomazcm.plantae.config.properties.MailProperties;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/email")
public class EmailApi {

    private final IngressoRepository ingressoRepo;
    private final MailProperties mailProperties;
    private final EmailApiService emailApiService;
    
    private static final String SURVEY_EMAIL_TEMPLATE = "surveyEmailTemplate";
    private static final String REMINDER_EMAIL_TEMPLATE = "reminderEmailTemplate";
    
    public EmailApi(IngressoRepository ingressoRepo, MailProperties mailProperties,
            EmailApiService emailApiService) {
        this.ingressoRepo = ingressoRepo;
        this.mailProperties = mailProperties;
        this.emailApiService = emailApiService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> getAllEmails() {
        return ResponseEntity.ok(getAllDistinctCustomerEmails());
    }

    @GetMapping("/send-survey")
    public ResponseEntity<?> sendSurveyEmail() {
        String subject = mailProperties.getSurveySubject();
        emailApiService.sendEmails(SURVEY_EMAIL_TEMPLATE, subject, getAllDistinctCustomerEmails());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/send-reminder")
    public ResponseEntity<?> sendReminderEmail() {
        String subject = mailProperties.getReminderSubject();
        emailApiService.sendEmails(REMINDER_EMAIL_TEMPLATE, subject, getAllDistinctCustomerEmails());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/list-bundles")
    public ResponseEntity<List<List<String>>> testBundles() {
        return ResponseEntity.ok(emailApiService.bundleCustomerEmails(getAllDistinctCustomerEmails()));
    }
    
    private List<String> getAllDistinctCustomerEmails() {
        return ingressoRepo.findAll().stream()
                .map(Ingresso::getEmail)
                .map(String::toLowerCase)
                .distinct().toList();
    }

}
