package com.thomazcm.plantae.controller;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thomazcm.plantae.controller.dto.IngressoDto;
import com.thomazcm.plantae.controller.dto.IngressoForm;
import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.repository.IngressoRepository;

@RestController
@RequestMapping("/ingressos")
public class IngressoController {

	@Autowired
	private IngressoRepository repository;

	@GetMapping
	public ResponseEntity<List<IngressoDto>> listarIngressos() {

		var ingressos = repository.findAll().stream().map(IngressoDto::new).collect(Collectors.toList());
		return ResponseEntity.ok(sortByValidoENumero(ingressos));
	}

	@GetMapping("/validar/{id}")
	public ResponseEntity<IngressoDto> validarPorNome(@PathVariable String id) {
		try {
			Ingresso ingresso = repository.findById(id).get();
			ingresso.setValid(false);
			repository.save(ingresso);
			return ResponseEntity.ok(new IngressoDto(ingresso));
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<IngressoDto> atualizar(@RequestBody IngressoForm form, @PathVariable String id) {
		try {
			Ingresso ingresso = repository.findById(id).get();
			ingresso.setCliente(form.getCliente());
			repository.save(ingresso);
			return ResponseEntity.ok(new IngressoDto(ingresso));
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<IngressoDto> excluir(@PathVariable String id) {
		repository.deleteById(id);
		return ResponseEntity.ok().build();
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
