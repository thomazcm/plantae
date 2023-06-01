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
		String qrcode = gerarQrCode(numero, senha);
		
		return repository.save(new Ingresso(numero, cliente, qrcode, senha));
	}

	private String gerarQrCode(int numero, int senha) {
		return apiEndpoint + "/qr-code/verificar?numero=" + numero + "&senha=" + senha;
	}
	
}
