package com.thomazcm.plantae.controller.dto;

import java.time.LocalDate;

import com.thomazcm.plantae.model.Ingresso;

public class IngressoDto {

    private int numero;
    private String id;
    private String cliente;
    private LocalDate data;
    private Boolean valid;

    public IngressoDto(Ingresso ingresso) {
        this.id = ingresso.getId();
        this.numero = ingresso.getNumero();
        this.cliente = ingresso.getCliente();
        this.data = ingresso.getData();
        this.valid = ingresso.getValid();
    }

    public int getNumero() {
        return numero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
