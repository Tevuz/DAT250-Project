package no.hvl.dat250.g13.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiAccessFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException exception) {
            String path = request.getRequestURI();
            if (path != null && path.startsWith("/api")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
            } else {
                throw exception;
            }
        }
    }
}
