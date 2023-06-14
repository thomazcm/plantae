package com.thomazcm.plantae.api;

import java.math.BigDecimal;
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
        newConfig.setLote(form.getLote());
        repository.save(newConfig);
        
        return ResponseEntity.ok(new ConfigurationDto(newConfig));
    }

}
