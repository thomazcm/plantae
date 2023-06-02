package com.thomazcm.plantae.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thomazcm.plantae.controller.dto.IngressoDto;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/ingressos")
public class IngressoController {

	@Autowired
	private IngressoRepository repository;

	@GetMapping
	public ResponseEntity<List<IngressoDto>> listarIngressos() {

		List<IngressoDto> ingressos = repository.findAll().stream().map(IngressoDto::new).collect(Collectors.toList());

		return ResponseEntity.ok(sortByValidoENumero(ingressos));
	}

	private List<IngressoDto> sortByValidoENumero(List<IngressoDto> ingressos) {
		List<IngressoDto> validos = ingressos.stream().filter(i -> i.getValid()).collect(Collectors.toList());
		List<IngressoDto> invalidos = ingressos.stream().filter(i -> !i.getValid()).collect(Collectors.toList());

		validos.sort((i1, i2) -> {
			return i1.getCliente().compareTo(i2.getCliente());
		});
		invalidos.sort((i1, i2) -> {
			return i1.getCliente().compareTo(i2.getCliente());
		});
		validos.addAll(invalidos);
		return validos;
	}

}
