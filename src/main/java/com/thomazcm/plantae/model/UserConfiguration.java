package com.thomazcm.plantae.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-config")
public class UserConfiguration {
    
    @Id
    private String id;
    private List<String> pixLinks;
    private BigDecimal unitPrice;
    private Integer maxTickets;
    
    public Integer getMaxTickets() {
        return this.maxTickets;
    }

    public void setMaxTickets(Integer maxTickets) {
        this.maxTickets = maxTickets;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public List<String> getPixLinks() {
        return Collections.unmodifiableList(this.pixLinks);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPixLinks(List<String> pixLinks) {
        this.pixLinks = pixLinks;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
}
