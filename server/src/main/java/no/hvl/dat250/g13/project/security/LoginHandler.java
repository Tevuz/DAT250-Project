package no.hvl.dat250.g13.project.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class LoginHandler {

    public static void onSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        //response.resetBuffer();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json");
        response.getWriter().append("{ \"message\": \"Login successful!\" }");
        response.flushBuffer();
    }

    public static void onFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception){
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
