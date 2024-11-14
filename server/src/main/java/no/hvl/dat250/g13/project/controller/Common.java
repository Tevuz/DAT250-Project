package no.hvl.dat250.g13.project.controller;

import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

public class Common {

    static ResponseEntity<?> responseOk(Result.Ok<?, ServiceError> result) {
        return ResponseEntity.status(HttpStatus.OK).body(result.value());
    }

    static ResponseEntity<?> responseCreated(Result.Ok<?, ServiceError> result, URI location) {
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(result.value());
    }

    static ResponseEntity<?> responseNoContent() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    static ResponseEntity<?> responseError(Result.Error<?, ServiceError> result) {
        return ResponseEntity.status(result.error().status().value()).body(result.error().message());
    }
}
