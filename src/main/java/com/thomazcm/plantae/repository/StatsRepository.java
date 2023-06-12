package com.thomazcm.plantae.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.Stats;

public interface StatsRepository extends MongoRepository<Stats, String>{

}
