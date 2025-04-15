package com.ticatag.assassement.air_quality_api.repositories;

import com.ticatag.assassement.air_quality_api.models.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;


@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findBySensorIdAndCapturedOnBetween(Long sensorId, LocalDateTime from, LocalDateTime to);

    Optional<Measurement> findTopBySensorIdOrderByCapturedOnDesc(Long sensorId);

}
