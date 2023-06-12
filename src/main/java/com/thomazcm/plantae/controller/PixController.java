package com.thomazcm.plantae.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
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
    public String linkPix(Model model, HttpServletRequest request) {
        model.addAttribute("apiEndpoint", apiEndpoint);
        
        if (statsRepository.findAll().size() == 0) {
            statsRepository.save(new Stats());
        }
        
        Integer maxTickets = configRepository.findAll().get(0).getMaxTickets();
        int totalTickets = ingressoRepository.findAll().size();
        
        Stats stats = statsRepository.findAll().get(0);
        String returnString = "link-pagamento";
        if (totalTickets >= maxTickets) {
            stats.setAcessosPaginaDeCompraEsgotados(stats.getAcessosPaginaDeCompraEsgotados()+1);
            returnString = "sold-out";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getName().compareTo("plantae") != 0) {
            stats.setAcessosPaginaDeCompraTotal(stats.getAcessosPaginaDeCompraTotal()+1);
            stats.getDatas().add(LocalDateTime.now().minus(Duration.ofHours(3L)));
            
            if (stats.getRequestIps() == null) {
                stats.setRequestIps(new ArrayList<String>());   
            }
            String clientIp = request.getHeader("X-Forwarded-For");
            if (clientIp == null || clientIp.isEmpty()) {
                clientIp = request.getRemoteAddr();
            }
            stats.getRequestIps().add(clientIp);
            statsRepository.save(stats);
        }
        model.addAttribute("remainingTickets", maxTickets-totalTickets);
        return returnString;
    }
}
