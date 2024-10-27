package no.hvl.dat250.g13.project.service.error;

import org.springframework.http.HttpStatus;

public record ServiceError(
        HttpStatus status,
        String message
) { }
