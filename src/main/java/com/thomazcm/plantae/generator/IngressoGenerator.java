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
	
	@Autowired IngressoRepository repository;
	
	public Ingresso novoIngresso(String nome, String email) {
		
		int numero = repository.findAll().size()+1;
		Ingresso novoIngresso = new Ingresso(numero, nome);
		repository.save(novoIngresso);
		
		String qrcode = gerarQrCode(novoIngresso.getId(), novoIngresso.getSenha());
		novoIngresso.setQrCodeUrl(qrcode);
		if (email != null && !email.isEmpty()) {
			novoIngresso.setEmail(email);
		}
		repository.save(novoIngresso);
		return novoIngresso;
	}

	private String gerarQrCode(String id, int senha) {
		return apiEndpoint + "/verificar?id=" + id + "&senha=" + senha;
	}
	
}
