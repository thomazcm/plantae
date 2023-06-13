package com.thomazcm.plantae.dto;

import com.thomazcm.plantae.model.Ingresso;

public class VerificacaoDto {

    private int numero;
    private String cliente;
    private Boolean valid;

    public VerificacaoDto(Ingresso ingresso) {
        this.numero = ingresso.getNumero();
        this.cliente = ingresso.getCliente();
        this.valid = ingresso.getValid();
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

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }
}
