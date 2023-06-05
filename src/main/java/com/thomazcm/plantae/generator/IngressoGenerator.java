package com.thomazcm.plantae.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@Service
public class IngressoGenerator {

    @Value("${plantae.endpoint.apiEndpoint}")
    private String apiEndpoint;

    @Autowired
    IngressoRepository repository;

    public Ingresso novoIngresso(String nome, String email) {

        int numero = repository.findAll().size() + 1;
        Ingresso novoIngresso = new Ingresso(numero, nome);
        repository.save(novoIngresso);

        novoIngresso.setQrCodeUrl(gerarQrCodeUrl(novoIngresso));
        if (email != null && !email.isEmpty()) {
            novoIngresso.setEmail(email);
        }
        return repository.save(novoIngresso);
    }

    private String gerarQrCodeUrl(Ingresso ingresso) {
        return apiEndpoint + "/verificar?id=" + ingresso.getId() + "&senha=" + ingresso.getSenha();
    }

}
