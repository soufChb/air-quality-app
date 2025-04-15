package com.ticatag.assassement.air_quality_api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "measurements")
public class Measurement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime capturedOn;

    @ManyToOne
    private Sensor sensor;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Metric> metrics = new ArrayList<>();

}
