package com.thomazcm.plantae.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thomazcm.plantae.controller.dto.UserConfigurationDto;
import com.thomazcm.plantae.model.UserConfiguration;
import com.thomazcm.plantae.repository.ConfigurationRepository;

@RestController
@RequestMapping("/configurations")
public class ConfigurationApi {
    
    private ConfigurationRepository repository;
    
    public ConfigurationApi(ConfigurationRepository repository) {
        this.repository = repository;
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
        
        newConfig.setPixLinks(form.getPixLinks());
        newConfig.setUnitPrice(BigDecimal.valueOf(form.getUnitPrice()));
        newConfig.setMaxTickets(32);
        repository.save(newConfig);
        
        return ResponseEntity.ok(new UserConfigurationDto(newConfig));
    }

}
