package com.example.weatherapiwebflux.service;

import com.example.weatherapiwebflux.model.ForecastAverage;
import reactor.core.publisher.Mono;

public interface CSVService {

    void initCSVWriter();
    Mono<ForecastAverage> printRecord(ForecastAverage forecastAverage);
    void closeCSVWriter();
}
