package com.example.weatherapiwebflux.controller.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;
}
