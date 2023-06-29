package com.thomazcm.plantae.api.form;

import java.math.BigDecimal;
import java.util.List;
import com.thomazcm.plantae.model.UserConfiguration;

public class UserConfigurationForm {
    
    private List<String> pixLinks;
    private Double unitPrice;
    private Integer maxTickets;
    
    public List<String> getPixLinks() {
        return this.pixLinks;
    }
    public Double getUnitPrice() {
        return this.unitPrice;
    }
    public Integer getMaxTickets() {
        return this.maxTickets;
    }
    
    public UserConfiguration updateConfiguration(UserConfiguration currentConfig) {
        UserConfiguration newConfig = new UserConfiguration();
        
        newConfig.setId(currentConfig.getId());
        newConfig.setLote(currentConfig.getLote());
        newConfig.setTextConfigurations(currentConfig.getTextConfigurations());
        newConfig.setMaxTickets(this.maxTickets);
        newConfig.setUnitPrice(new BigDecimal(this.unitPrice));
        newConfig.setPixLinks(this.pixLinks);
        
        return newConfig;
    }
    
}
