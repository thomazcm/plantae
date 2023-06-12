package com.thomazcm.plantae.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PixController {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;
    
    @GetMapping("/link-pix")
    public String linkPix(Model model) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        return "link-pagamento";
    }
    
}
