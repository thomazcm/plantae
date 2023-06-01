package com.thomazcm.plantae.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.thomazcm.plantae.model.Ingresso;


public interface IngressoRepository extends MongoRepository<Ingresso, String>{

	Optional<Ingresso> findByNumero(int parseInt);

}
