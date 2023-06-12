package com.thomazcm.plantae.controller.dto;

import java.util.ArrayList;
import java.util.List;
import com.thomazcm.plantae.model.UserConfiguration;

public class UserConfigurationDto {

    private List<String> pixLinks;
    private Double unitPrice;
    private Integer maxTickets;
    
    public UserConfigurationDto() {};
    
    public UserConfigurationDto(UserConfiguration userConfig) {
        this.pixLinks = new ArrayList<>(userConfig.getPixLinks());
        this.unitPrice = userConfig.getUnitPrice().doubleValue();
        this.maxTickets = userConfig.getMaxTickets();
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
}
