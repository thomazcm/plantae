package com.thomazcm.plantae.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.thomazcm.plantae.controller.helper.ControllerHelper;
import com.thomazcm.plantae.dto.config.PublicConfigurationDto;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.repository.StatsRepository;

@Controller
public class PixController {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;
    
    private ConfigurationRepository configRepository;
    private StatsRepository statsRepository;
    private ControllerHelper helper;
    
    public PixController(ConfigurationRepository configRepository, StatsRepository statsRepository, ControllerHelper ControllerHelper) {
        this.configRepository = configRepository;
        this.statsRepository = statsRepository;
        this.helper = ControllerHelper;
    }

    @GetMapping("/link-pix")
    public String linkPix(Model model, HttpServletRequest request) {
        
        var stats = statsRepository.getStats();
        var config = configRepository.getConfig();
        int remaining = helper.calculateRemaniningTickets();
        var publicConfig = new PublicConfigurationDto(remaining, config);
        
        String returnString = "link-pagamento";
        if (remaining <= 0) {
            returnString = "sold-out";
            stats.novoAcessoEsgotado();
            
            publicConfig.addConfig("soldOutText1", config);
            publicConfig.addConfig("soldOutText2", config);
        }
        
        stats.novoAcesso(request);
        statsRepository.save(stats);
        
        model.addAttribute("config", publicConfig);
        return returnString;
    }

}
