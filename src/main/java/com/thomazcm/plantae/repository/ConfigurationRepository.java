package com.thomazcm.plantae.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.UserConfiguration;

public interface ConfigurationRepository extends MongoRepository<UserConfiguration, String>{

    default UserConfiguration getConfig() {
        List<UserConfiguration> currentConfig = this.findAll();
        if (currentConfig.size() == 0) {
            createDefaultConfig();
            currentConfig = this.findAll();
        }
        return currentConfig.get(0);
    }

    private void createDefaultConfig() {
        UserConfiguration defaultConfig = new UserConfiguration(
                new ArrayList<String>(List.of("","","","")),
                new BigDecimal(27), 
                30);
        this.save(defaultConfig);
    }
    
}
