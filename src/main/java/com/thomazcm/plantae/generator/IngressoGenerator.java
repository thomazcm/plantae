package com.thomazcm.plantae.generator;

import java.util.Random;

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
		int senha = new Random().nextInt(1, Integer.MAX_VALUE);
		Ingresso novoIngresso = new Ingresso(numero, cliente, senha);
		repository.save(novoIngresso);
		String qrcode = gerarQrCode(novoIngresso.getId(), senha);
		novoIngresso.setQrCodeUrl(qrcode);
		repository.save(novoIngresso);
		return novoIngresso;
	}

	private String gerarQrCode(String id, int senha) {
		return apiEndpoint + "/verificar?id=" + id + "&senha=" + senha;
	}
	
}
