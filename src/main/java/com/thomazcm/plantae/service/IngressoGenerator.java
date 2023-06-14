package com.thomazcm.plantae.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.model.LoteIngresso;
import com.thomazcm.plantae.repository.ConfigurationRepository;
import com.thomazcm.plantae.repository.IngressoRepository;

@Service
public class IngressoGenerator {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;

    IngressoRepository ingressoRepository;
    ConfigurationRepository configurationRepository;

    public IngressoGenerator(IngressoRepository ingressoRepository,
            ConfigurationRepository configurationRepository) {
        this.ingressoRepository = ingressoRepository;
        this.configurationRepository = configurationRepository;
    }

    public Ingresso novoIngresso(String nome, String email, Boolean cortesia) {

        int numero = ingressoRepository.findAll().size() + 1;
        Ingresso novoIngresso = new Ingresso(numero, nome);
        definirLote(novoIngresso, cortesia);
        ingressoRepository.save(novoIngresso);

        novoIngresso.setQrCodeUrl(gerarQrCodeUrl(novoIngresso));
        if (email != null && !email.isEmpty()) {
            novoIngresso.setEmail(email);
        }
        return ingressoRepository.save(novoIngresso);
    }

    private void definirLote(Ingresso ingresso, Boolean cortesia) {
        if (cortesia) {
            ingresso.setLote(LoteIngresso.CORTESIA);
            return;
        }
        int loteAtual = configurationRepository.getConfig().getLote();
        switch (loteAtual) {
            case 2: {
                ingresso.setLote(LoteIngresso.SEGUNDO);
                break;
            }
            case 3: {
                ingresso.setLote(LoteIngresso.TECEIRO);
                break;
            }
            default:
                ingresso.setLote(LoteIngresso.PRIMEIRO);
        }
    }

    private String gerarQrCodeUrl(Ingresso ingresso) {
        return apiEndpoint + "/verificar?id=" + ingresso.getId() + "&senha=" + ingresso.getSenha();
    }

}
