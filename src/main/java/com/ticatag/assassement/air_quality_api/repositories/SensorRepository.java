package com.ticatag.assassement.air_quality_api.repositories;

import com.ticatag.assassement.air_quality_api.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
