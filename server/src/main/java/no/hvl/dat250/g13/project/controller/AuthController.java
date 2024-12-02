package no.hvl.dat250.g13.project.controller;

import no.hvl.dat250.g13.project.service.UserService;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static no.hvl.dat250.g13.project.controller.Common.responseError;
import static no.hvl.dat250.g13.project.controller.Common.responseOk;

@RestController
@RequestMapping("/account")
@CrossOrigin
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<?> getLogIn(Authentication authentication) {
        //assert (authentication.isAuthenticated()): "Invalid security chain, require authenticated user";

        logger.debug("👤 Username: {}", "");

        String username = switch (authentication) {
            case UserDetails login -> login.getUsername();
            case OAuth2AuthenticationToken login -> login.getPrincipal().getAttribute("login");
            default -> "";
        };


        return switch (userService.readUser(new UserId(username))) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

//    @PostMapping("/auth")
//    public ResponseEntity<?> postLogIn() {
//        return ResponseEntity.ok().build();
//    }
}
