package com.thomazcm.plantae.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thomazcm.plantae.model.UserConfiguration;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.repository.IngressoRepository;
import com.thomazcm.plantae.repository.StatsRepository;

@Controller
public class PixController {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;
    
    private ConfigurationRepository configRepository;
    private StatsRepository statsRepository;
    private IngressoRepository ingressoRepository;
    
    public PixController(ConfigurationRepository configRepository, IngressoRepository ingressoRepository, StatsRepository statsRepository) {
        this.configRepository = configRepository;
        this.ingressoRepository = ingressoRepository;
        this.statsRepository = statsRepository;
    }

    @GetMapping("/link-pix")
    public String linkPix(Model model, HttpServletRequest request) {
        
        var stats = statsRepository.getStats();
        var config = configRepository.getConfig();
        int remaining = calculateRemaniningTickets(config);
        
        String returnString = "link-pagamento";
        if (remaining <= 0) {
            returnString = "sold-out";
            stats.novoAcessoEsgotado();
        }
        
        if (isAuthenticated()) {
            stats.novoAcesso(request);
            statsRepository.save(stats);
        }
        
        model.addAttribute("config", config);
        model.addAttribute("remainingTickets", remaining);
        return returnString;
    }

    private int calculateRemaniningTickets(UserConfiguration config) {
        int totalTicketsSold = ingressoRepository.findAll().size();
        int maxTickets = config.getMaxTickets();
        return maxTickets - totalTicketsSold;
    }
    
    private Boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName().compareTo("plantae") != 0;
    }
}
