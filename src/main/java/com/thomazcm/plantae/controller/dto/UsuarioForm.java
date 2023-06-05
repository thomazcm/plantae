package com.thomazcm.plantae.controller.dto;

import java.time.LocalDate;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.thomazcm.plantae.model.Usuario;

public class UsuarioForm {

    private String email;
    private String senha;
    private String nome;

    public Usuario toUser(BCryptPasswordEncoder encoder) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setUsername(email);
        usuario.setPassword(encoder.encode(senha));
        usuario.setDataCriacao(LocalDate.now());
        return usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
