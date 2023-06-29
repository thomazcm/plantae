package com.thomazcm.plantae.api.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thomazcm.plantae.model.integration.Cliente;

public class PedidoDto {

    @JsonProperty("clientes")
    private List<Cliente> clientes;
    private String email;
    private Boolean cortesia;

    public Boolean getCortesia() {
        return this.cortesia;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public String getEmail() {
        return email;
    }

}
