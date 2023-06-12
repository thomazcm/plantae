package com.thomazcm.plantae.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.repository.IngressoRepository;

@Controller
public class PixController {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;
    
    private ConfigurationRepository configRepository;
    private IngressoRepository ingressoRepository;
    
    public PixController(ConfigurationRepository configRepository, IngressoRepository ingressoRepository) {
        this.configRepository = configRepository;
        this.ingressoRepository = ingressoRepository;
    }

    @GetMapping("/link-pix")
    public String linkPix(Model model) {
        model.addAttribute("apiEndpoint", apiEndpoint);

        if (ingressoRepository.findAll().size() >= configRepository.findAll().get(0).getMaxTickets()) {
            return "sold-out";
        }
        return "link-pagamento";
    }
}
