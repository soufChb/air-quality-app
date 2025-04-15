package com.ticatag.assassement.air_quality_api.dtos;


import com.ticatag.assassement.air_quality_api.models.Metric;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MeasurementRequest {

    @NotNull(message = "Sensor ID is required")
    private Long sensorId;

    @NotNull(message = "Date must not be null")
    private LocalDateTime capturedOn;

    @NotEmpty(message = "At least one metric is required")
    private List<Metric> metrics;

}
