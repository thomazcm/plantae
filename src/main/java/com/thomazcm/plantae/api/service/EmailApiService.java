package com.thomazcm.plantae.api.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailApiService {

    private final PlantaeEmailSender emailSender;
    private static final int BUNDLE_MAX_SIZE = 5;
    private static final int THREAD_WAIT_MILLISECONDS = 3000;
    private static final String REMINDER_EMAIL_TEMPLATE = "reminderEmailTemplate";
    

    public EmailApiService(PlantaeEmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void sendEmails(String template, String subject, List<String> allCustomerEmails) {
        var emailBundles = bundleCustomerEmails(allCustomerEmails);

        for (List<String> bundle : emailBundles) {
            bundle.stream().forEach(email -> {
                emailSender.sendEmailTemplate(email, subject, REMINDER_EMAIL_TEMPLATE);
                System.out.println("Sent mail to: " + email);
                try {
                    Thread.sleep(THREAD_WAIT_MILLISECONDS);
                } catch (InterruptedException e) {
                    System.out.println("Thread error waiting for next email bundle");
                    e.printStackTrace();
                }
            });
        }
    }

    public List<List<String>> bundleCustomerEmails(List<String> allCustomerEmails) {
        var emailBundles = new ArrayList<List<String>>();
        List<String> bundle = new ArrayList<String>();

        for (String customerEmail : allCustomerEmails) {
            bundle.add(customerEmail);
            if (bundle.size() >= BUNDLE_MAX_SIZE) {
                emailBundles.add(List.copyOf(bundle));
                bundle = new ArrayList<String>();
            }
        }
        if (!bundle.isEmpty() && bundle.size() < BUNDLE_MAX_SIZE) {
            emailBundles.add(List.copyOf(bundle));
        }
        return emailBundles;
    }

}
