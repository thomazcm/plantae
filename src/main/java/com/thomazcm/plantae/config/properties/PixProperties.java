package com.thomazcm.plantae.config.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.springframework.stereotype.Component;

@Component
public class PixProperties {

    private ResourceBundle resourceBundle;

    public PixProperties(ResourceBundle resourceBundle) {
        this.resourceBundle = resourceBundle;
    }
    
    private String getResource(String resource) {
        return this.resourceBundle.getString("plantae.pix." + resource);
    }
    
    public List<String> getPixList() {
        List<String> pixLinks = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            pixLinks.add(getResource("link"+i));
        }
        return pixLinks;
    }
    
}
