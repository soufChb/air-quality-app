package com.ticatag.assassement.air_quality_api.services.Impl;

import com.ticatag.assassement.air_quality_api.Exceptions.ResourceNotFoundException;
import com.ticatag.assassement.air_quality_api.dtos.MeasurementRequest;
import com.ticatag.assassement.air_quality_api.dtos.SensorMetricResponse;
import com.ticatag.assassement.air_quality_api.models.Measurement;
import com.ticatag.assassement.air_quality_api.models.Metric;
import com.ticatag.assassement.air_quality_api.models.Sensor;
import com.ticatag.assassement.air_quality_api.repositories.MeasurementRepository;
import com.ticatag.assassement.air_quality_api.repositories.SensorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceImplTest {

    @InjectMocks
    private MeasurementServiceImpl measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorRepository sensorRepository;


    private final Long sensorId = 1L;
    private final LocalDateTime from = LocalDateTime.of(2025, 4, 10, 0, 0);
    private final LocalDateTime to = LocalDateTime.of(2025, 4, 12, 23, 59);

    @Test
    void shouldReturnAverageForMetricInDateRange() {
        Metric m1 = new Metric();
        m1.setName("CO2");
        m1.setValue(10.0);

        Metric m2 = new Metric();
        m2.setName("CO2");
        m2.setValue(20.0);

        Measurement meas1 = new Measurement();
        meas1.setCapturedOn(LocalDateTime.of(2025, 4, 11, 10, 0));
        meas1.setMetrics(List.of(m1));

        Measurement meas2 = new Measurement();
        meas2.setCapturedOn(LocalDateTime.of(2025, 4, 11, 15, 0));
        meas2.setMetrics(List.of(m2));

        when(measurementRepository.findBySensorIdAndCapturedOnBetween(sensorId, from, to))
                .thenReturn(List.of(meas1, meas2));

        List<SensorMetricResponse> result = measurementService.filterMeasurements(
                List.of(sensorId), List.of("CO2"), "avg", from, to
        );

        assertEquals(1, result.size());
        Metric aggregatedMetric = result.get(0).getMetrics().get(0);
        assertEquals("CO2", aggregatedMetric.getName());
        assertEquals(15.0, aggregatedMetric.getValue());
    }

    @Test
    void shouldReturnMinForMetricInDateRange() {
        Metric m1 = new Metric();
        m1.setName("CO2");
        m1.setValue(30.0);

        Metric m2 = new Metric();
        m2.setName("CO2");
        m2.setValue(15.0);

        Measurement meas1 = new Measurement();
        meas1.setMetrics(List.of(m1));
        Measurement meas2 = new Measurement();
        meas2.setMetrics(List.of(m2));

        when(measurementRepository.findBySensorIdAndCapturedOnBetween(sensorId, from, to))
                .thenReturn(List.of(meas1, meas2));

        List<SensorMetricResponse> result = measurementService.filterMeasurements(
                List.of(sensorId), List.of("CO2"), "min", from, to
        );

        Metric aggregated = result.get(0).getMetrics().get(0);
        assertEquals(15.0, aggregated.getValue());
    }

    @Test
    void shouldReturnFilteredMetricsForSensor() {
        Long sensorId = 1L;
        Metric co2 = new Metric();
        co2.setName("CO2");
        co2.setValue(15.0);

        Metric temp = new Metric();
        temp.setName("TEMP");
        temp.setValue(22.5);

        Measurement measurement = new Measurement();
        measurement.setCapturedOn(LocalDateTime.now());
        measurement.setMetrics(List.of(co2, temp));

        when(measurementRepository.findTopBySensorIdOrderByCapturedOnDesc(sensorId))
                .thenReturn(Optional.of(measurement));

        List<SensorMetricResponse> result = measurementService.filterMeasurements(
                List.of(sensorId), List.of("CO2")
        );

        assertEquals(1, result.size());
        SensorMetricResponse response = result.get(0);
        assertEquals(sensorId, response.getSensorId());
        assertEquals(1, response.getMetrics().size());
        assertEquals("CO2", response.getMetrics().get(0).getName());
    }

    @Test
    void shouldReturnAllMetricsWhenMetricListIsNull() {
        Long sensorId = 2L;
        Metric co2 = new Metric();
        co2.setName("CO2");
        co2.setValue(10.0);

        Metric pm25 = new Metric();
        pm25.setName("PM2.5");
        pm25.setValue(5.0);

        Measurement measurement = new Measurement();
        measurement.setCapturedOn(LocalDateTime.now());
        measurement.setMetrics(List.of(co2, pm25));

        when(measurementRepository.findTopBySensorIdOrderByCapturedOnDesc(sensorId))
                .thenReturn(Optional.of(measurement));

        List<SensorMetricResponse> result = measurementService.filterMeasurements(
                List.of(sensorId), null
        );

        assertEquals(1, result.size());
        SensorMetricResponse response = result.get(0);
        assertEquals(sensorId, response.getSensorId());
        assertEquals(2, response.getMetrics().size());
    }

    @Test
    void shouldSaveMeasurementSuccessfully() {
        Long sensorId = 1L;
        Sensor sensor = new Sensor();
        sensor.setId(sensorId);

        MeasurementRequest dto = new MeasurementRequest();
        dto.setSensorId(sensorId);
        dto.setCapturedOn(LocalDateTime.now());

        Metric metric1 = new Metric();
        metric1.setName("CO2");
        metric1.setValue(10.0);

        Metric metric2 = new Metric();
        metric2.setName("PM2.5");
        metric2.setValue(5.0);
        dto.setMetrics(List.of(metric1, metric2));

        when(sensorRepository.findById(sensorId)).thenReturn(Optional.of(sensor));

        when(measurementRepository.save(any(Measurement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Measurement saved = measurementService.saveMeasurement(dto);

        assertNotNull(saved);
        assertEquals(sensor, saved.getSensor());
        assertEquals(2, saved.getMetrics().size());
        assertEquals("CO2", saved.getMetrics().get(0).getName());
    }

    @Test
    void shouldThrowExceptionWhenSensorNotFound() {
        Long sensorId = 99L;

        MeasurementRequest dto = new MeasurementRequest();
        dto.setSensorId(sensorId);
        dto.setCapturedOn(LocalDateTime.now());

        when(sensorRepository.findById(sensorId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class,
                () -> measurementService.saveMeasurement(dto));

        assertEquals("Sensor not found with ID: " + sensorId, thrown.getMessage());
    }





}