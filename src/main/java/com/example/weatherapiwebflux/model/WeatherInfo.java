package com.example.weatherapiwebflux.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WeatherInfo {

    private Integer temperature;
    private Integer wind;
    private String description;
    private List<Forecast> forecast;

}
