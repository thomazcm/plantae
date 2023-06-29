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
        return currentConfig;
    }

    
    @SuppressWarnings("unused")
    private UserConfiguration updateConfigurationFields(UserConfiguration currentConfig) {
        var updatedConfig = new UserConfiguration();
        updatedConfig.setId(currentConfig.getId());
        updatedConfig.setMaxTickets(currentConfig.getMaxTickets());
        updatedConfig.setPixLinks(currentConfig.getPixLinks());
        updatedConfig.setUnitPrice(currentConfig.getUnitPrice());
        updatedConfig.setTextConfigurations(currentConfig.getTextConfigurations());
        updatedConfig.setLote(currentConfig.getLote());
        return updatedConfig;
    }

    private void createDefaultConfig() {
        UserConfiguration defaultConfig = new UserConfiguration(
                new ArrayList<String>(List.of("","","","")),
                new BigDecimal(27), 
                30, new HashMap<String, String>(), 1);
        defaultConfig.getTextConfigurations().put("", "");
        this.save(defaultConfig);
    }
    
}
