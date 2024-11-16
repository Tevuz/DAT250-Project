package no.hvl.dat250.g13.project.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import no.hvl.dat250.g13.project.service.UserService;
import no.hvl.dat250.g13.project.service.data.user.UserCreate;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.data.user.UserUpdate;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static no.hvl.dat250.g13.project.controller.Common.*;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.USERNAME_PATTERN;
import static no.hvl.dat250.g13.project.service.data.validation.Constraints.USER_ID_PATTERN;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreate info) {
        return switch (userService.createUser(info)) {
            case Result.Ok<?, ServiceError> result -> responseCreated(result, URI.create(""));
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<?> readUser(
            @Parameter(required = true, schema =
                @Schema(type = "string", pattern = USERNAME_PATTERN + "|" + USER_ID_PATTERN), examples =
                    {@ExampleObject(name="Username", value="user"), @ExampleObject(name="Id", value="id:1")})
            @PathVariable String identifier
    ) throws BindException {
        var info = UserId.parse(identifier);
        info.validate();
        return switch (userService.readUser(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/{identifier}/votes")
    public ResponseEntity<?> readUserVotes(
            @Parameter(required = true, schema =
            @Schema(type = "string", pattern = USERNAME_PATTERN + "|" + USER_ID_PATTERN), examples =
                    {@ExampleObject(name="Username", value="user"), @ExampleObject(name="Id", value="id:1")})
            @PathVariable String identifier
    ) throws BindException {
        var info = UserId.parse(identifier);
        info.validate();
        return switch (userService.readUserVotes(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/{identifier}/surveys")
    public ResponseEntity<?> readUserSurveys(
            @Parameter(required = true, schema =
            @Schema(type = "string", pattern = USERNAME_PATTERN + "|" + USER_ID_PATTERN), examples =
                    {@ExampleObject(name="Username", value="user"), @ExampleObject(name="Id", value="id:1")})
            @PathVariable String identifier
    ) throws BindException {
        var info = UserId.parse(identifier);
        info.validate();
        return switch (userService.readUserSurveys(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping
    public ResponseEntity<?> readAllUsers() {
        // TODO: implement pagination
        return switch (userService.readAllUsers()) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdate info) {
        return switch (userService.updateUser(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<?> deleteUser(
            @Parameter(required = true, schema =
            @Schema(type = "string", pattern = USERNAME_PATTERN + "|" + USER_ID_PATTERN), examples =
                    {@ExampleObject(name="Username", value="username"), @ExampleObject(name="Id", value="id:1")})
            @PathVariable String identifier
    ) throws BindException {
        var info = UserId.parse(identifier);
        info.validate();
        return switch (userService.deleteUser(info)) {
            case Result.Ok<?, ServiceError> ignored -> responseNoContent();
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

}
