package com.thomazcm.plantae.controller.helper;

import org.springframework.stereotype.Service;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.repository.IngressoRepository;

@Service
public class ControllerHelper {
    
    private IngressoRepository ingressoRepo;
    private ConfigurationRepository configRepo;
    
    public ControllerHelper(IngressoRepository ingressoRepo, ConfigurationRepository configRepo) {
        this.ingressoRepo = ingressoRepo;
        this.configRepo = configRepo;
    }

    public int calculateRemaniningTickets() {
        int totalTicketsSold = ingressoRepo.findAll().size();
        int maxTickets = configRepo.getConfig().getMaxTickets();
        return maxTickets - totalTicketsSold;
    }

    public int getMaxTickets() {
        return configRepo.getConfig().getMaxTickets();
    }
}
