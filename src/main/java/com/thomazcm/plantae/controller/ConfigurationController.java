package com.thomazcm.plantae.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user-configuration")
public class ConfigurationController {
    
    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;

    @GetMapping
    public String userConfiguration(Model model) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        return "user-configuration";
    }
    
}
