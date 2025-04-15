package com.ticatag.assassement.air_quality_api;

import com.ticatag.assassement.air_quality_api.models.Sensor;
import com.ticatag.assassement.air_quality_api.repositories.SensorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(SensorRepository sensorRepository) {

        return args -> {
            log.info("Preloading " + sensorRepository.save(new Sensor("Lannion")));
            log.info("Preloading " + sensorRepository.save(new Sensor("Paris")));
            log.info("Preloading " + sensorRepository.save(new Sensor("Lyon")));
            log.info("Preloading " + sensorRepository.save(new Sensor("Marseille")));
        };
    }
}
