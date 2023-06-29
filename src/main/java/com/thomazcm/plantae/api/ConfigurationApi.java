package com.thomazcm.plantae.api;

import java.math.BigDecimal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.api.dto.UserConfigurationDto;
import com.thomazcm.plantae.api.form.UserConfigurationForm;
import com.thomazcm.plantae.api.service.ConfigApiService;
import com.thomazcm.plantae.api.service.PlantaeEmailSender;
import com.thomazcm.plantae.model.UserConfiguration;
import com.thomazcm.plantae.repository.ConfigurationRepository;

@RestController
@RequestMapping("/configurations")
public class ConfigurationApi {
    
    private final ConfigurationRepository repository;
    private final ConfigApiService configService;
    private final PlantaeEmailSender mailService;

    public ConfigurationApi(ConfigurationRepository repository, PlantaeEmailSender mailService, ConfigApiService configService) {
        this.repository = repository;
        this.configService = configService;
        this.mailService = mailService;
    }

    @GetMapping
    public ResponseEntity<UserConfigurationDto> getConfig() {
        return ResponseEntity.ok(new UserConfigurationDto(repository.getConfig()));
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
    public ResponseEntity<?> updateConfig(@RequestBody UserConfigurationForm form) {
        
        UserConfiguration currentConfig = repository.getConfig();
        String warningMessage = configService.buildWarningMessage(form, currentConfig);
        
        if (configService.configHasChanged(warningMessage)) {
            configService.sendConfigChangeWarningEmails(warningMessage, mailService);
            UserConfiguration newConfig = form.updateConfiguration(currentConfig);
            repository.save(newConfig);
        }

        return ResponseEntity.ok(new UserConfigurationDto(currentConfig));
    }


}
