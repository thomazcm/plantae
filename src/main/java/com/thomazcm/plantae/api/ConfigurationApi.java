package com.thomazcm.plantae.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.dto.UserConfigurationDto;
import com.thomazcm.plantae.model.UserConfiguration;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.service.EmailService;

@RestController
@RequestMapping("/configurations")
public class ConfigurationApi {
    
    private ConfigurationRepository repository;
    private EmailService mailService;
    
    @Value("${plantae.email.adminMail1}")
    private String adminMail1;
    @Value("${plantae.email.adminMail2}")
    private String adminMail2;
    
    public ConfigurationApi(ConfigurationRepository repository, EmailService mailService) {
        this.repository = repository;
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<UserConfigurationDto> getConfig() {
        var userConfiguration = repository.findAll().get(0);
        return ResponseEntity.ok(new UserConfigurationDto(userConfiguration));
    }
    
    @PostMapping
    public ResponseEntity<?> creteConfig(@RequestBody UserConfigurationDto form) {
        UserConfiguration userConfig = new UserConfiguration();
        userConfig.setPixLinks(form.getPixLinks());
        userConfig.setUnitPrice(BigDecimal.valueOf(form.getUnitPrice()));
        
        repository.save(userConfig);
        return ResponseEntity.ok(new UserConfigurationDto(userConfig));
    }
    
    @PutMapping
    public ResponseEntity<?> updateConfig(@RequestBody UserConfigurationDto form) {
        List<UserConfiguration> currentConfigList = repository.findAll();
        
        UserConfiguration newConfig;
        if (repository.findAll().size() < 1) {
            newConfig = new UserConfiguration();
        } else {
            newConfig = currentConfigList.get(0);
        }
        
        StringBuilder builder = new StringBuilder("Alguém alterou as configurações Plantae:\n");
        if (newConfig.getUnitPrice().doubleValue() != form.getUnitPrice()) {
            builder.append(" -Preço do ingresso\n");
        }
        if (newConfig.getMaxTickets() != form.getMaxTickets()) {
            builder.append(" -Número máximo de ingressos\n");
        }
        for (int i = 0; i < newConfig.getPixLinks().size(); i++) {
            if (newConfig.getPixLinks().get(i).compareTo(form.getPixLinks().get(i)) != 0) {
                builder.append(" -Link Pix "+ (i+1) +"\n");
            }
        }
        if (builder.toString().compareTo("Alguém alterou as configurações Plantae:\n") != 0) {
            mailService.sendEmail(adminMail1, "Alerta - Configurações Alteradas Plantae", builder.toString());
            mailService.sendEmail(adminMail2, "Alerta - Configurações Alteradas Plantae", builder.toString());
        }
        
        newConfig.setPixLinks(form.getPixLinks());
        newConfig.setUnitPrice(BigDecimal.valueOf(form.getUnitPrice()));
        newConfig.setMaxTickets(form.getMaxTickets());
        repository.save(newConfig);
        
        return ResponseEntity.ok(new UserConfigurationDto(newConfig));
    }

}
