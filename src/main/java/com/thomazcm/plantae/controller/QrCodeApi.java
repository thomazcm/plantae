package com.thomazcm.plantae.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thomazcm.plantae.controller.dto.RequestPayload;
import com.thomazcm.plantae.controller.service.EmailService;
import com.thomazcm.plantae.generator.IngressoGenerator;
import com.thomazcm.plantae.generator.PdfGenerator;
import com.thomazcm.plantae.generator.QRCodeGenerator;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/qr-code")
public class QrCodeApi {

	@Autowired IngressoGenerator ingressoGenerator;
	@Autowired QRCodeGenerator qrCodeGenerator;
	@Autowired IngressoRepository repository;
	@Autowired PdfGenerator pdfGenerator;
	@Autowired EmailService emailSender;
	
	@GetMapping("/mail")
	public ResponseEntity<?> sendMail (){
		emailSender.sendEmail("contato.plantaecozinhavegetal@gmail.com", "email vindo do app...", "testando email pelo app");
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "/novo", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> novoIngresso(@RequestBody RequestPayload payload) throws Exception {

		var ingresso = ingressoGenerator.novoIngresso(payload.getClienteDto());
		String nome = ingresso.getCliente();
		String nomeIngresso = ajustarNomeArquivo(nome);
		var qrCodeImage = qrCodeGenerator.generateQRCodeImage(ingresso.getQrCodeUrl());
		ByteArrayOutputStream baos = pdfGenerator.createPDF(qrCodeImage, nome);
		
		if(ingresso.getEmail() != null) {
			emailSender.sendPdfEmail(ingresso, baos, nomeIngresso);
		}
		return ResponseEntity.ok().headers(pdfDownloadHeaders(nomeIngresso)).body(baos.toByteArray());
	}

	@GetMapping("/pdf/{id}")
	public ResponseEntity<byte[]> baixarIngresso(@PathVariable String id) throws Exception {
	
		var ingresso = repository.findById(id).get();
		String nome = ingresso.getCliente();
		String nomeIngresso = ajustarNomeArquivo(nome);
		var qrCodeImage = qrCodeGenerator.generateQRCodeImage(ingresso.getQrCodeUrl());
		ByteArrayOutputStream baos = pdfGenerator.createPDF(qrCodeImage, nome);

		return ResponseEntity.ok().headers(pdfDownloadHeaders(nomeIngresso)).body(baos.toByteArray());
	}

	private HttpHeaders pdfDownloadHeaders(String nomeIngresso) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDisposition(ContentDisposition.attachment().filename(nomeIngresso).build());
		return headers;
	}

	private String ajustarNomeArquivo(String nome) {
		String nomeIngresso;
		try {
			nomeIngresso = "entrada-" + nome.substring(0, nome.indexOf(" "))+"-plantae.pdf";
		} catch (IndexOutOfBoundsException e) {
			nomeIngresso = "entrada-" + nome +"-plantae.pdf";
		}
		return nomeIngresso;
	}
}
