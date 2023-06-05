package com.thomazcm.plantae.controller.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thomazcm.plantae.model.Cliente;

public class ClienteDto {

    @JsonProperty("clientes")
    private List<Cliente> clientes;
    private String email;


    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
