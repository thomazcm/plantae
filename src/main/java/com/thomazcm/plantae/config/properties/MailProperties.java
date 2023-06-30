package com.thomazcm.plantae.config.properties;

import java.util.ResourceBundle;
import org.springframework.stereotype.Component;

@Component
public class MailProperties {
    
    private ResourceBundle resourceBundle;

    public MailProperties(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
    
    private String getResource(String resource) {
        return this.resourceBundle.getString("plantae.mail." + resource);
    }
    
    public String getSender() {
        return this.getResource("sender");
    }
    
    public String getSubject() {
        return this.getResource("subject");
    }
    
    public String getSurveySubject() {
        return this.getResource("surveySubject");
    }
    
    public String getReminderSubject() {
        return this.getResource("reminderSubject");
    }

}
