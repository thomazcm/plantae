package com.thomazcm.plantae.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thomazcm.plantae.config.properties.PixProperties;

@Controller
public class PixController {

    private PixProperties properties;
    
    public PixController(PixProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/link-pix")
    public String linkPix(Model model) {
        model.addAttribute("pixLinks", properties.getPixList());
        return "link-pagamento";
    }
    
}
