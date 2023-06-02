package com.thomazcm.plantae.controller.dto;

import java.time.LocalDate;

import com.thomazcm.plantae.model.Ingresso;

public class IngressoDto {

	private int numero;
	private String cliente;
	private LocalDate data;
	private Boolean valid;
	
	public IngressoDto(Ingresso ingresso) {
		this.numero = ingresso.getNumero();
		this.cliente = ingresso.getCliente();
		this.data = ingresso.getData();
		this.valid = ingresso.getValid();
	}
	public int getNumero() {
		return numero;
	}
	public String getCliente() {
		return cliente;
	}
	public LocalDate getData() {
		return data;
	}
	public Boolean getValid() {
		return valid;
	}
	
}
