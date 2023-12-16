package com.example.client;

import com.example.weatherapiwebflux.model.ForecastAverage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class WebClientAPI {

    private WebClient webClient;

    public WebClientAPI() {
        this.webClient = WebClient.builder()
                                  .baseUrl("http://localhost:8080")
                                  .build();
    }

    public Flux<ForecastAverage> getWeatherForecast() {
        log.info("Getting weather forecast...");
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/weather")
                        .queryParam("city", "Cluj-Napoca,Bucuresti,Timisoara,Constanta,Baia-Mare,Arad")
                        .build())
                .retrieve()
                .bodyToFlux(ForecastAverage.class);
    }

    public static void main(String[] args) throws IOException {
        WebClientAPI api = new WebClientAPI();

        List<ForecastAverage> results = api.getWeatherForecast().collect(Collectors.toList()).share().block();
        log.info("API call result:");
        results.forEach(r -> log.info(r.toString()));

        log.info("CSV file content:" + System.lineSeparator() + new String(Files.readAllBytes(Paths.get("weather_forecast.csv"))));
    }

}
