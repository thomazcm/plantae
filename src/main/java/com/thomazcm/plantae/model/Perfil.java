package com.thomazcm.plantae.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
public class Perfil implements GrantedAuthority {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
