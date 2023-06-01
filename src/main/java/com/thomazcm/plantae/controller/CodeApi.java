package com.thomazcm.plantae.controller;

import java.awt.image.BufferedImage;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thomazcm.plantae.controller.dto.ClienteDto;
import com.thomazcm.plantae.controller.dto.VerificacaoDto;
import com.thomazcm.plantae.generator.IngressoGenerator;
import com.thomazcm.plantae.generator.QRCodeGenerator;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/qr-code")
public class CodeApi {
	
	@Autowired IngressoGenerator ingressoGenerator;
	@Autowired QRCodeGenerator qrCodeGenerator;
	@Autowired IngressoRepository repository;
	
	@PostMapping(value = "/novo", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<BufferedImage> QRCode(@RequestBody ClienteDto cliente)
	throws Exception{
		var ingresso = ingressoGenerator.novoIngresso(cliente.getNome());
		return ResponseEntity.ok(qrCodeGenerator.generateQRCodeImage(ingresso.getQrCodeUrl()));
	}
	
	@GetMapping("/verificar")
	public ResponseEntity<VerificacaoDto> verificarQrCode(
			@RequestParam(required = false) String numero, @RequestParam(required = false) String senha){
		
		Optional<Ingresso> ingressoOptional = repository.findByNumero(Integer.parseInt(numero));
		
		Ingresso ingresso;
		try {
			ingresso = ingressoOptional.get();
			if (!ingresso.verificarSenha(senha)) {
				throw new NoSuchElementException();
			}
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(new VerificacaoDto(ingresso));
	}
}
