package com.thomazcm.plantae.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document(collection = "users")
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String email;
    private String nome;
    @JsonIgnore
    private Collection<Perfil> perfis = new ArrayList<>();
    @JsonIgnore
    private String senha;
    @JsonIgnore
    private String token;
    @JsonIgnore
    private LocalDateTime tokenExpiration;
    private LocalDate dataCriacao;

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setUsername(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    public void setPassword(String senha) {
        this.senha = senha;
    }

    public Collection<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Collection<Perfil> perfis) {
        this.perfis = perfis;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return perfis;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getToken() {
        return token;
    }

    public Usuario setToken(String token) {
        this.token = token;
        this.tokenExpiration = LocalDateTime.now().plusMinutes(19);
        return this;
    }

    public boolean tokenExpirado() {
        try {
            return LocalDateTime.now().isAfter(tokenExpiration);
        } catch (NullPointerException e) {
            return true;
        }
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

}
