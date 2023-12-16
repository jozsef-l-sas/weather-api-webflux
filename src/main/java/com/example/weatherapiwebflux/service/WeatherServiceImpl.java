package com.example.weatherapiwebflux.service;

import com.example.weatherapiwebflux.model.Forecast;
import com.example.weatherapiwebflux.model.ForecastAverage;
import com.example.weatherapiwebflux.model.WeatherInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private static final String CITIES_SEPARATOR = ",";

    private final WebClient webClient;
    private final CSVService csvService;

    public WeatherServiceImpl(WebClient.Builder webClientBuilder, @Value("${weather.api}") String weatherApi, CSVService csvService) {
        this.webClient = webClientBuilder.baseUrl(weatherApi).build();
        this.csvService = csvService;
    }

    @Override
    public Mono<ForecastAverage> getCityWeatherInfo(String city, String originalName) {
        log.info("Retrieving weather forecast for city {}", originalName);
        return this.webClient.get().uri("/{city}", city)
                             .retrieve()
                             .bodyToMono(WeatherInfo.class)
                             .map(weatherInfo -> ForecastAverage.builder()
                                                                .name(originalName)
                                                                .temperature(
                                                                        String.valueOf((int) Math.round(
                                                                                weatherInfo.getForecast().stream()
                                                                                           .mapToInt(Forecast::getTemperature)
                                                                                           .average().getAsDouble())
                                                                        )
                                                                )
                                                                .wind(
                                                                        String.valueOf((int) Math.round(
                                                                                weatherInfo.getForecast().stream()
                                                                                           .mapToInt(Forecast::getWind)
                                                                                           .average().getAsDouble())
                                                                        )
                                                                )
                                                                .build())
                             .onErrorResume(WebClientResponseException.class,
                                     ex -> ex.getRawStatusCode() == 404
                                             ? Mono.just(ForecastAverage.builder().name(originalName).build())
                                             : Mono.error(ex));
    }

    @Override
    public Flux<ForecastAverage> getWeatherInfo(String citiesString) {
        Map<String, String> originalNames = new HashMap<>();
        List<String> cities = Stream.of(citiesString.split(CITIES_SEPARATOR))
                                    .map(c -> {
                                        c = c.trim();
                                        String sanitizedName = c.toLowerCase();
                                        originalNames.putIfAbsent(sanitizedName, c);
                                        return sanitizedName;
                                    })
                                    .distinct()
                                    .toList();
        return Flux.fromIterable(cities).log()
                   .doFirst(() -> csvService.initCSVWriter())
                   .flatMap(c -> getCityWeatherInfo(c, originalNames.get(c)))
                   .sort(Comparator.comparing(ForecastAverage::getName))
                   .flatMap(fa -> csvService.printRecord(fa))
                   .doFinally(signalType -> csvService.closeCSVWriter());
    }
}
