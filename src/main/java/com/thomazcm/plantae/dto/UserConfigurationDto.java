package com.thomazcm.plantae.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.thomazcm.plantae.model.UserConfiguration;

public class UserConfigurationDto {

    private List<String> pixLinks;
    private Double unitPrice;
    private Integer maxTickets;
    private HashMap<String, String> textConfigurations;
    
    public UserConfigurationDto() {};
    
    public UserConfigurationDto(UserConfiguration userConfig) {
        this.pixLinks = new ArrayList<>(userConfig.getPixLinks());
        this.unitPrice = userConfig.getUnitPrice().doubleValue();
        this.maxTickets = userConfig.getMaxTickets();
    }
    
    public void setTextConfigurations(HashMap<String, String> textConfigurations) {
        this.textConfigurations = textConfigurations;
    }

    public HashMap<String, String> getTextConfigurations() {
        return this.textConfigurations;
    }

    public List<String> getPixLinks() {
        return this.pixLinks;
    }
    public Double getUnitPrice() {
        return this.unitPrice;
    }

    public Integer getMaxTickets() {
        return maxTickets;
    }

    public void setMaxTickets(int maxTickets) {
        this.maxTickets = maxTickets;
        
    }
}
