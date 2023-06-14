package com.thomazcm.plantae.dto.config;

import java.util.List;

public class ConfigurationUpdateForm {

    private List<String> pixLinks;
    private Double unitPrice;
    private Integer maxTickets;
    private Integer lote;
    
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

    public Integer getLote() {
        return this.lote;
    }
    
}
