package com.ticatag.assassement.air_quality_api.services;

import com.ticatag.assassement.air_quality_api.dtos.MeasurementRequest;
import com.ticatag.assassement.air_quality_api.dtos.SensorMetricResponse;
import com.ticatag.assassement.air_quality_api.models.Measurement;

import java.time.LocalDateTime;
import java.util.List;


public interface MeasurementService {

    Measurement saveMeasurement(MeasurementRequest measurement);

    List<Measurement> findAll();

    List<SensorMetricResponse> filterMeasurements(List<Long> sensorIds, List<String> metrics);

    List<SensorMetricResponse> filterMeasurements(List<Long> sensorIds, List<String> metrics, String stat, LocalDateTime from, LocalDateTime to);

}
