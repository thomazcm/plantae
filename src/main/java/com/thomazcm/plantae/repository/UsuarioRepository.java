package com.thomazcm.plantae.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.config.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByEmail(String email);
    
}
