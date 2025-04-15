package com.ticatag.assassement.air_quality_api.controllers;

import com.ticatag.assassement.air_quality_api.Exceptions.IllegalRequestParamException;
import com.ticatag.assassement.air_quality_api.dtos.MeasurementRequest;
import com.ticatag.assassement.air_quality_api.dtos.SensorMetricResponse;
import com.ticatag.assassement.air_quality_api.models.Measurement;
import com.ticatag.assassement.air_quality_api.services.MeasurementService;
import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/measurements")
    public Measurement add(@Valid @RequestBody MeasurementRequest request) {
        return measurementService.saveMeasurement(request);
    }

    @GetMapping("/measurements/all")
    public ResponseEntity<List<Measurement>> getAll() {
        return ResponseEntity.ok(measurementService.findAll());
    }


    @GetMapping("/measurements")
    public ResponseEntity<List<SensorMetricResponse>> find(
            @RequestParam() List<Long> sensorIds,
            @RequestParam(required = false) List<String> metrics,
            @RequestParam(required = false) String stat,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to

    ) {

        List<String> validStats = List.of("avg", "min", "max", "sum");

        if (stat != null && !validStats.contains(stat.toLowerCase())) {
            throw new IllegalRequestParamException("Invalid stat: " + stat);
        }

        List<SensorMetricResponse> result;

        if (to == null || from == null) {
            result = measurementService.filterMeasurements(sensorIds, metrics);
        } else {
            result = measurementService.filterMeasurements(sensorIds, metrics, stat, from, to);
        }

        return ResponseEntity.ok(result);

    }



}
