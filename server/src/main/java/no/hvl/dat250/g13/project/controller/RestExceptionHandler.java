package no.hvl.dat250.g13.project.controller;

import no.hvl.dat250.g13.project.security.ApiAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.Serializable;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    public record Response(String message, List<String> details) implements Serializable {}

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Response> handleInvalidBody(BindException exception) {
        var details = exception.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(new Response("Validation Failed", details));
    }

    @ExceptionHandler(ApiAccessDeniedException.class)
    public ResponseEntity<Response> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response(exception.getMessage(), List.of()));
    }
}
