package com.thomazcm.plantae.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.UserConfiguration;

public interface ConfigurationRepository extends MongoRepository<UserConfiguration, String>{

    default UserConfiguration getConfig() {
        List<UserConfiguration> currentConfigList = this.findAll();
        if (currentConfigList.size() == 0) {
            createDefaultConfig();
            currentConfigList = this.findAll();
        }
        
        var currentConfig = currentConfigList.get(0);
        
        if (currentConfig.getTextConfigurations() == null) {
            currentConfig.setTextConfigurations(new HashMap<String, String>());
            currentConfig.getTextConfigurations().put("", "");
            this.save(currentConfig);
            currentConfig = this.findAll().get(0);
        }
        return currentConfig;
    }

    private void createDefaultConfig() {
        UserConfiguration defaultConfig = new UserConfiguration(
                new ArrayList<String>(List.of("","","","")),
                new BigDecimal(27), 
                30, new HashMap<String, String>());
        this.save(defaultConfig);
    }
    
}
