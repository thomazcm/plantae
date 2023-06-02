package com.thomazcm.plantae.model;

import java.time.LocalDate;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Ingresso {

	private Integer numero;
	private String cliente;
	private LocalDate data;
	private final String qrCodeUrl;
	private Boolean valid = true;
	private int senha;
	
	public Ingresso(int numero, String cliente, String qrCodeUrl, int senha) {
		this.numero = numero;
		this.cliente = cliente;
		this.data = LocalDate.now();
		this.qrCodeUrl = qrCodeUrl;
		this.senha = senha;
	}
	
	public Boolean verificarSenha(String senha) {
		if (this.senha == Integer.parseInt(senha)) {
			return true;
		}
		return false;
	}
	
	public void setSenha(int senha) {
		this.senha = senha;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
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

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}
}
