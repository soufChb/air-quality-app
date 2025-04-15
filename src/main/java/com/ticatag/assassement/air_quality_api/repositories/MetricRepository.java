package com.ticatag.assassement.air_quality_api.repositories;

import com.ticatag.assassement.air_quality_api.dtos.SensorMetricResponse;
import com.ticatag.assassement.air_quality_api.models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
}
