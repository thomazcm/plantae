package com.thomazcm.plantae.api;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.dto.config.ConfigurationDto;
import com.thomazcm.plantae.model.config.UserConfiguration;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.service.EmailService;

@RestController
@RequestMapping("/configurations")
public class ConfigurationApi {

    @Value("${plantae.email.adminMail1}")
    private String adminMail1;
    @Value("${plantae.email.adminMail2}")
    private String adminMail2;
    private static final String EMAIL_WARNING_SUBJECT = "Alerta - Configurações Alteradas Plantae";
    private static final String EMAIL_WARNING_BASE = "Alguém alterou as configurações Plantae:\n";

    private final ConfigurationRepository repository;
    private final EmailService mailService;

    public ConfigurationApi(ConfigurationRepository repository, EmailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<ConfigurationDto> getConfig() {
        return ResponseEntity.ok(new ConfigurationDto(repository.getConfig()));
    }

    @PostMapping
    public ResponseEntity<?> creteConfig(@RequestBody ConfigurationDto form) {
        UserConfiguration userConfig = new UserConfiguration();
        userConfig.setPixLinks(form.getPixLinks());
        userConfig.setUnitPrice(BigDecimal.valueOf(form.getUnitPrice()));

        repository.save(userConfig);
        return ResponseEntity.ok(new ConfigurationDto(userConfig));
    }

    @PutMapping
    public ResponseEntity<?> updateConfig(@RequestBody ConfigurationDto form) {

        UserConfiguration newConfig = repository.getConfig();

        StringBuilder builder = new StringBuilder(EMAIL_WARNING_BASE);
        if (newConfig.getUnitPrice().doubleValue() != form.getUnitPrice()) {
            builder.append(" -Preço do ingresso\n");
        }
        if (newConfig.getMaxTickets() != form.getMaxTickets()) {
            builder.append(" -Número máximo de ingressos\n");
        }
        for (int i = 0; i < newConfig.getPixLinks().size(); i++) {
            if (newConfig.getPixLinks().get(i).compareTo(form.getPixLinks().get(i)) != 0) {
                builder.append(" -Link Pix " + (i + 1) + "\n");
            }
        }
        
        String finalMessage = builder.toString();
        if (finalMessage.equals(EMAIL_WARNING_BASE)) {
            List.of(adminMail1, adminMail2).parallelStream().forEach(adminMail -> {
                mailService.sendEmail(adminMail, EMAIL_WARNING_SUBJECT, finalMessage);
            });
        }

        newConfig.setPixLinks(form.getPixLinks());
        newConfig.setUnitPrice(BigDecimal.valueOf(form.getUnitPrice()));
        newConfig.setMaxTickets(form.getMaxTickets());
        repository.save(newConfig);

        return ResponseEntity.ok(new ConfigurationDto(newConfig));
    }

}
