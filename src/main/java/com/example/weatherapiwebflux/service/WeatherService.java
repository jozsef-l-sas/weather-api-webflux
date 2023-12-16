package com.example.weatherapiwebflux.service;

import com.example.weatherapiwebflux.model.ForecastAverage;
import com.example.weatherapiwebflux.model.WeatherInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WeatherService {

    Mono<ForecastAverage> getCityWeatherInfo(String city, String originalName);
    Flux<ForecastAverage> getWeatherInfo(String citiesString);
}
