package com.example.weatherapiwebflux.controller;

import com.example.weatherapiwebflux.model.ForecastAverage;
import com.example.weatherapiwebflux.service.WeatherService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather")
    public Flux<ForecastAverage> getWeatherInfo(@RequestParam @NotBlank String city) {
        return this.weatherService.getWeatherInfo(city);
    }
}
