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
	
	public Ingresso novoIngresso(String nomeCliente) {
		
		int numero = repository.findAll().size()+1;
		String cliente = nomeCliente;
		Ingresso novoIngresso = new Ingresso(numero, cliente);
		repository.save(novoIngresso);
		String qrcode = gerarQrCode(novoIngresso.getId(), novoIngresso.getSenha());
		novoIngresso.setQrCodeUrl(qrcode);
		repository.save(novoIngresso);
		return novoIngresso;
	}

	private String gerarQrCode(String id, int senha) {
		return apiEndpoint + "/verificar?id=" + id + "&senha=" + senha;
	}
	
}
