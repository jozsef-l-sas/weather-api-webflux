package com.example.weatherapiwebflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forecast {

    private Integer day;
    private Integer temperature;
    private Integer wind;

}
