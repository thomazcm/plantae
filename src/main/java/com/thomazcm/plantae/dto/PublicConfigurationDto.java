package com.thomazcm.plantae.dto;

import java.util.HashMap;
import java.util.List;
import com.thomazcm.plantae.model.UserConfiguration;

public class PublicConfigurationDto {

    private Double unitPrice;
    private Integer remainingTickets;
    private List<String> pixLinks;
    private HashMap<String, String> textConfigurations = new HashMap<String, String>();
    
    public PublicConfigurationDto(Integer remainingTickets, UserConfiguration userConfig) {
        
        this.unitPrice = userConfig.getUnitPrice().doubleValue();
        this.remainingTickets = remainingTickets > 4 ? 4 : remainingTickets;
        this.pixLinks = userConfig.getPixLinks();
        
    }

    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public Integer getRemainingTickets() {
        return this.remainingTickets;
    }

    public List<String> getPixLinks() {
        return this.pixLinks;
    }

    public HashMap<String, String> getTextConfigurations() {
        return this.textConfigurations;
    }

    public void addConfig(String key, UserConfiguration config) {
       String value = config.getTextConfigurations().get(key);
       this.textConfigurations.put(key, value);
    }
    
}
