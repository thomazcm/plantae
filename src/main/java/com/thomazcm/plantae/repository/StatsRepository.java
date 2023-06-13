package com.thomazcm.plantae.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.thomazcm.plantae.model.Stats;

public interface StatsRepository extends MongoRepository<Stats, String>{

    default Stats getStats() {
        List<Stats> currentStats = this.findAll();
        if (currentStats.size() == 0) {
            this.save(new Stats());
            currentStats = this.findAll();
        }
        return currentStats.get(0);
    }

}
