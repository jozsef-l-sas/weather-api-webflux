package com.example.weatherapiwebflux.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForecastAverage {

    private String name;
    @Builder.Default
    private String temperature = "";
    @Builder.Default
    private String wind = "";
}
