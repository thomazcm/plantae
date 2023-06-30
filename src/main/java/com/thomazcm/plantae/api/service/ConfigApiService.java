package com.thomazcm.plantae.api.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.thomazcm.plantae.api.form.UserConfigurationForm;
import com.thomazcm.plantae.model.UserConfiguration;

@Service
public class ConfigApiService {
    
    private static final String EMAIL_WARNING_SUBJECT = "Alerta - Configurações Alteradas Plantae";
    private static final String EMAIL_WARNING_BASE = "Alguém alterou as configurações Plantae:\n";
    @Value("${plantae.email.adminMail1}")
    private String adminMail1;
    @Value("${plantae.email.adminMail2}")
    private String adminMail2;
    
    
    public void sendConfigChangeWarningEmails(String finalMessage, PlantaeEmailSender mailService) {
        List.of(adminMail1, adminMail2).stream().forEach(adminMail -> {
            mailService.sendEmail(adminMail, EMAIL_WARNING_SUBJECT, finalMessage);
        });
    }

    public String buildWarningMessage(UserConfigurationForm form,
            UserConfiguration currentConfig) {
        
        StringBuilder builder = new StringBuilder(EMAIL_WARNING_BASE);
        if (currentConfig.getUnitPrice().doubleValue() != form.getUnitPrice()) {
            builder.append(" -Preço do ingresso\n");
        }
        if (currentConfig.getMaxTickets() != form.getMaxTickets()) {
            builder.append(" -Número máximo de ingressos\n");
        }
        for (int i = 0; i < currentConfig.getPixLinks().size(); i++) {
            if (currentConfig.getPixLinks().get(i).compareTo(form.getPixLinks().get(i)) != 0) {
                builder.append(" -Link Pix " + (i + 1) + "\n");
            }
        }
        return builder.toString();
    }

    public boolean configHasChanged(String finalMessage) {
        return !finalMessage.equals(EMAIL_WARNING_BASE);
    }

}
