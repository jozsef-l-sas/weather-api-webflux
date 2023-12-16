package com.example.weatherapiwebflux.service;

import com.example.weatherapiwebflux.model.ForecastAverage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.FileWriter;
import java.io.IOException;

@Service
@Slf4j
public class CSVServiceImpl implements CSVService {

    private final String[] csvHeaders;
    private final String filename;
    private FileWriter fileWriter;
    private CSVPrinter csvPrinter;
    private boolean isInitialized;

    public CSVServiceImpl(@Value("${csv.headers}") String[] csvHeaders, @Value("${csv.filename}") String filename) {
        this.csvHeaders = csvHeaders;
        this.filename = filename;
    }

    @Override
    public void initCSVWriter() {
        try {
            this.fileWriter = new FileWriter(filename);
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                                                   .setHeader(csvHeaders)
                                                   .build();
            this.csvPrinter = new CSVPrinter(fileWriter, csvFormat);
            this.isInitialized = true;
            log.info("Initialized CSV file writer.");
        } catch (IOException e) {
            this.isInitialized = false;
            log.error("Error creating CSV printer", e);
        }
    }

    @Override
    public Mono<ForecastAverage> printRecord(ForecastAverage forecastAverage) {
        if (!this.isInitialized) {
            throw new IllegalStateException("CSVPrinter is not initialized.");
        }
        try {
            this.csvPrinter.printRecord(forecastAverage.getName(), forecastAverage.getTemperature(), forecastAverage.getWind());
            log.info("Printed CSV row: {}", forecastAverage);
            return Mono.just(forecastAverage);
        } catch (IOException e) {
            log.error("Error writing to CSV file", e);
            closeCSVWriter();
        }
        return null;
    }

    @Override
    public void closeCSVWriter() {
        if (this.csvPrinter != null) {
            try {
                this.csvPrinter.flush();
                this.csvPrinter.close();
                log.info("Closed CSV file printer.");
            } catch (IOException e) {
                log.error("Error closing CSV printer", e);
            }
        }
    }
}
