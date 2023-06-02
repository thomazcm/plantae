package com.thomazcm.plantae.controller;

import java.io.ByteArrayOutputStream;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thomazcm.plantae.controller.dto.RequestPayload;
import com.thomazcm.plantae.controller.dto.VerificacaoDto;
import com.thomazcm.plantae.generator.IngressoGenerator;
import com.thomazcm.plantae.generator.PdfGenerator;
import com.thomazcm.plantae.generator.QRCodeGenerator;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/qr-code")
public class QrCodeApi {
	
	@Autowired IngressoGenerator ingressoGenerator;
	@Autowired QRCodeGenerator qrCodeGenerator;
	@Autowired IngressoRepository repository;
	@Autowired PdfGenerator pdfGenerator;
	
	@PostMapping(value = "/novo", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> QRCode(@RequestBody RequestPayload payload)
	throws Exception{
		
		String nome = payload.getClienteDto().getNome();
		var ingresso = ingressoGenerator.novoIngresso(nome);
		var qrCodeImage = qrCodeGenerator.generateQRCodeImage(ingresso.getQrCodeUrl());
		ByteArrayOutputStream baos = pdfGenerator.createPDF(qrCodeImage, nome);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDisposition(ContentDisposition.attachment()
	    		.filename("ingresso-plantae.pdf").build());
	    
	    return ResponseEntity.ok().headers(headers).body(baos.toByteArray());
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
