package com.ticatag.assassement.air_quality_api.dtos;

import com.ticatag.assassement.air_quality_api.models.Metric;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorMetricResponse {


        private Long sensorId;
        private List<Metric> metrics;




}
