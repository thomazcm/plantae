package com.thomazcm.plantae.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.UserConfiguration;

public interface ConfigurationRepository extends MongoRepository<UserConfiguration, String>{

}
