package com.thomazcm.plantae.model;

import java.time.LocalDate;
import java.util.Random;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "ingressos")
public class Ingresso {

	@Id
	private String id;
	private Integer numero;
	private String cliente;
	private LocalDate data;
	private String qrCodeUrl;
	private Boolean valid = true;
	private int senha;
	
	public Ingresso(int numero, String cliente) {
		this.numero = numero;
		this.cliente = cliente;
		this.data = LocalDate.now();
		this.senha = new Random().nextInt(1, Integer.MAX_VALUE);
	}
	
	public void setQrCodeUrl(String qrCodeUrl) {
		this.qrCodeUrl = qrCodeUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Boolean validar(int senha) {
		if (this.valid && this.senha == senha) {
			this.valid = false;
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
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public LocalDate getData() {
		return data;
	}

	public Boolean getValid() {
		return valid;
	}

	public String getQrCodeUrl() {
		return qrCodeUrl;
	}

	public int getSenha() {
		return senha;
	}
}
