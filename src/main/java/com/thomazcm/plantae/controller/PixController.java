package com.thomazcm.plantae.controller;

import java.time.Duration;
import java.time.LocalDateTime;import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thomazcm.plantae.model.Stats;
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
    public String linkPix(Model model) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        String returnString = "link-pagamento";
        
        if (statsRepository.findAll().size() == 0) {
            statsRepository.save(new Stats());
        }
        Stats stats = statsRepository.findAll().get(0);

        stats.setAcessosPaginaDeCompraTotal(stats.getAcessosPaginaDeCompraTotal()+1);
        
        if (ingressoRepository.findAll().size() >= configRepository.findAll().get(0).getMaxTickets()) {
            
            stats.setAcessosPaginaDeCompraEsgotados(stats.getAcessosPaginaDeCompraEsgotados()+1);
            returnString = "sold-out";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().compareTo("plantae") != 0) {
            stats.getDatas().add(LocalDateTime.now().minus(Duration.ofHours(3L)));
            statsRepository.save(stats);
        }
        return returnString;
    }
}
