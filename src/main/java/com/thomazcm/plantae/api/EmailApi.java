package com.thomazcm.plantae.api;

import java.util.List;
import org.apache.regexp.RE;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.api.service.EmailApiService;
import com.thomazcm.plantae.api.service.PlantaeEmailSender;
import com.thomazcm.plantae.config.properties.MailProperties;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/email")
public class EmailApi {

    private final IngressoRepository ingressoRepo;
    private final MailProperties mailProperties;
    private final EmailApiService emailApiService;
    private final PlantaeEmailSender sender;
    
    private static final String SURVEY_EMAIL_TEMPLATE = "surveyEmailTemplate";
    private static final String REMINDER_EMAIL_TEMPLATE = "reminderEmailTemplate";
    
    public EmailApi(IngressoRepository ingressoRepo, MailProperties mailProperties,
            EmailApiService emailApiService, PlantaeEmailSender sender) {
        this.ingressoRepo = ingressoRepo;
        this.mailProperties = mailProperties;
        this.emailApiService = emailApiService;
        this.sender = sender;
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
    
//    @GetMapping("/send-reminder-test")
//    public ResponseEntity<String> sendReminderTestMail() {
//        String subject = mailProperties.getReminderSubject();
//        List<String> emailsMissing = List.of("thomazcm@gmail.com");
//        
//        for (String email : emailsMissing) {
//            sender.sendEmailTemplate(email, subject, REMINDER_EMAIL_TEMPLATE);
//            System.out.println("Sent mail to: " + email);
//        }
//        return ResponseEntity.ok("sent");
//    }
    
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
