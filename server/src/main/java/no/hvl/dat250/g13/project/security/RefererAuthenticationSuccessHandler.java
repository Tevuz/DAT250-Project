package no.hvl.dat250.g13.project.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;

public class RefererAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private RequestCache requestCache = new HttpSessionRequestCache();

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        logger.info("RefererAuthenticationSuccessHandler");
        handle(request, response);
        clearAuthenticationAttributes(request);
    }

    protected final void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        if (savedRequest != null) {
            String targetUrl = savedRequest.getHeaderValues("Referer").getFirst();
            logger.trace("Using url {} from Referer header", targetUrl);
            if (response.isCommitted()) {
                logger.debug("Did not redirect to {} since response already committed.", targetUrl);
                return;
            }

            this.redirectStrategy.sendRedirect(request, response, targetUrl);
        }
    }

    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
