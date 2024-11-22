package no.hvl.dat250.g13.project.security;

import java.io.IOException;

public class ApiAccessDeniedException extends IOException {
    public ApiAccessDeniedException(String message) {
        super(message);
    }
}
