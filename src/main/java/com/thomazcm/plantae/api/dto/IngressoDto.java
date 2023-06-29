package com.thomazcm.plantae.api.dto;

import java.time.LocalDate;

import com.thomazcm.plantae.model.Ingresso;
import com.thomazcm.plantae.model.LoteIngresso;

public class IngressoDto {

    private int numero;
    private String id;
    private String cliente;
    private LocalDate data;
    private Boolean valid;
    private LoteIngresso lote;

    public IngressoDto(Ingresso ingresso) {
        this.id = ingresso.getId();
        this.numero = ingresso.getNumero();
        this.cliente = ingresso.getCliente();
        this.data = ingresso.getData();
        this.valid = ingresso.getValid();
        this.lote = ingresso.getLote();
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
    
    public LoteIngresso getLote() {
        return this.lote;
    }

}
