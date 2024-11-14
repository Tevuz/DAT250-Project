package no.hvl.dat250.g13.project.controller;

import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.net.URI;

public class Common {

    static <T> ResponseEntity<?> responseOk(Result.Ok<T, ServiceError> result) {
        return ResponseEntity.status(HttpStatus.OK).body(result.value());
    }

    static <T> ResponseEntity<?> responseCreated(Result.Ok<T, ServiceError> result, URI location) {
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(result.value());
    }

    static <T> ResponseEntity<?> responseNoContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    static <T> ResponseEntity<?> responseError(Result.Error<T, ServiceError> result) {
        return ResponseEntity.status(result.error().status().value()).body(result.error().message());
    }
}
