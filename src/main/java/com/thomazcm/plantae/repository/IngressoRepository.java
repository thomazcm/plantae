package com.thomazcm.plantae.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.Ingresso;


public interface IngressoRepository extends MongoRepository<Ingresso, String> {
}
