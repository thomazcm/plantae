package com.thomazcm.plantae.dto;

import java.util.HashMap;
import java.util.List;
import com.thomazcm.plantae.model.UserConfiguration;

public class PublicConfigurationDto {

    private Double unitPrice;
    private Integer remainingTickets;
    private List<String> pixLinks;
    private HashMap<String, String> textConfigurations;
    
    public PublicConfigurationDto(Integer remainingTickets, UserConfiguration userConfig) {
        
        this.unitPrice = userConfig.getUnitPrice().doubleValue();
        this.remainingTickets = remainingTickets > 4 ? 4 : remainingTickets;
        this.pixLinks = userConfig.getPixLinks();
        this.textConfigurations = userConfig.getTextConfigurations();
        
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
    
    
}
