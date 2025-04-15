package com.ticatag.assassement.air_quality_api.services.Impl;

import com.ticatag.assassement.air_quality_api.Exceptions.IllegalRequestParamException;
import com.ticatag.assassement.air_quality_api.Exceptions.ResourceNotFoundException;
import com.ticatag.assassement.air_quality_api.dtos.MeasurementRequest;
import com.ticatag.assassement.air_quality_api.dtos.SensorMetricResponse;
import com.ticatag.assassement.air_quality_api.models.Measurement;
import com.ticatag.assassement.air_quality_api.models.Metric;
import com.ticatag.assassement.air_quality_api.models.Sensor;
import com.ticatag.assassement.air_quality_api.repositories.MeasurementRepository;
import com.ticatag.assassement.air_quality_api.repositories.SensorRepository;
import com.ticatag.assassement.air_quality_api.services.MeasurementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementServiceImpl implements MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    public MeasurementServiceImpl(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Measurement saveMeasurement(MeasurementRequest dto) {

        Sensor sensor = sensorRepository.findById(dto.getSensorId())
                .orElseThrow(() -> new ResourceNotFoundException("Sensor not found with ID: " + dto.getSensorId()));

        Measurement measurement = new Measurement();
        measurement.setCapturedOn(dto.getCapturedOn());
        measurement.setSensor(sensor);

        List<Metric> metricList = dto.getMetrics().stream()
                .map(mt -> {
                    Metric m = new Metric();
                    m.setName(mt.getName());
                    m.setValue(mt.getValue());
                    return m;
                })
                .collect(Collectors.toList());

        measurement.setMetrics(metricList);

        return measurementRepository.save(measurement);
    }

    @Override
    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }


    @Override
    public List<SensorMetricResponse> filterMeasurements(List<Long> sensorIds, List<String> metrics) {
        List<SensorMetricResponse> result = new ArrayList<>();

        for (Long sensorId : sensorIds) {

            Optional<Measurement> optionalMeasurement = measurementRepository
                    .findTopBySensorIdOrderByCapturedOnDesc(sensorId);

            if (optionalMeasurement.isEmpty()) continue;

            Measurement latestMeasurement = optionalMeasurement.get();

            if(metrics != null) {
                List<Metric> filteredMetrics = filterByMetrics(latestMeasurement, metrics);
                result.add(new SensorMetricResponse(sensorId, filteredMetrics));
            } else {
                result.add(new SensorMetricResponse(sensorId, latestMeasurement.getMetrics()));
            }

        }

        return result;
    }

    private List<Metric> filterByMetrics(Measurement measurement, List<String> metricNames) {
        return measurement.getMetrics().stream()
                .filter(metric -> metricNames.contains(metric.getName()))
                .toList();
    }


    @Override
    public List<SensorMetricResponse> filterMeasurements(
            List<Long> sensorIds,
            List<String> metricNames,
            String stat,
            LocalDateTime from,
            LocalDateTime to) {

        return sensorIds.stream()
                .map(sensorId -> {
                    List<Measurement> measurements = getMeasurements(sensorId, from, to);
                    List<Metric> aggregatedMetrics = metricNames.stream()
                            .map(metricName -> aggregateMetricByStat(stat, metricName, measurements))
                            .filter(Objects::nonNull)
                            .toList();

                    return new SensorMetricResponse(sensorId, aggregatedMetrics);
                })
                .toList();
    }

    private List<Measurement> getMeasurements(Long sensorId, LocalDateTime from, LocalDateTime to) {
        return measurementRepository.findBySensorIdAndCapturedOnBetween(sensorId, from, to);
    }

    private Metric aggregateMetricByStat(String stat, String metricName, List<Measurement> measurements) {

        List<Metric> metrics = measurements.stream()
                .flatMap(m -> m.getMetrics().stream())
                .filter(m -> m.getName().equalsIgnoreCase(metricName))
                .toList();

        if (metrics.isEmpty()) return null;

        double aggregatedValue = switch (stat.toLowerCase()) {
            case "avg" -> metrics.stream().mapToDouble(Metric::getValue).average().orElse(Double.NaN);
            case "min" -> metrics.stream().mapToDouble(Metric::getValue).min().orElse(Double.NaN);
            case "max" -> metrics.stream().mapToDouble(Metric::getValue).max().orElse(Double.NaN);
            case "sum" -> metrics.stream().mapToDouble(Metric::getValue).sum();
            default -> throw new IllegalRequestParamException("Invalid stat: " + stat);
        };

        Metric result = new Metric();
        result.setName(metricName);
        result.setValue(aggregatedValue);
        return result;
    }

}
