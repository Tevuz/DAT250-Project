package no.hvl.dat250.g13.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ApiAccessFilter extends OncePerRequestFilter {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            String referer = request.getHeader("Referer");
            logger.info("🚧 Access filter: { path: {}, referer: {} }", path, referer);
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException exception) {
            String path = request.getRequestURI();
            if (false && path != null && path.startsWith("/api")) {
                //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
            } else {
                throw exception;
            }
        }
    }
}
