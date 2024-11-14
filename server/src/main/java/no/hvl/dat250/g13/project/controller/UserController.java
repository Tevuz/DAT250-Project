package no.hvl.dat250.g13.project.controller;

import jakarta.validation.Valid;
import no.hvl.dat250.g13.project.service.UserService;
import no.hvl.dat250.g13.project.service.data.user.UserCreate;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.data.user.UserUpdate;
import no.hvl.dat250.g13.project.service.data.user.UserUsername;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static no.hvl.dat250.g13.project.controller.Common.*;

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

    @GetMapping("/{username}")
    public ResponseEntity<?> readUserByUsername(@PathVariable String username) throws BindException {
        var info = new UserUsername(username);
        info.validate();
        return switch (userService.readUserByUsername(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> readUserByUsername(@PathVariable Long id) throws BindException {
        var info = new UserId(id);
        info.validate();
        return switch (userService.readUserById(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/{username}/votes")
    public ResponseEntity<?> readUserVotesByUsername(@PathVariable String username) throws BindException {
        var info = new UserUsername(username);
        info.validate();
        return switch (userService.readUserVotesByUsername(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/{username}/surveys")
    public ResponseEntity<?> readUserSurveysByUsername(@PathVariable String username) throws BindException {
        var info = new UserUsername(username);
        info.validate();
        return switch (userService.readUserSurveysByUsername(info)) {
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

    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdate info) throws BindException {
        if (info.id() != null && !id.equals(info.id()))
            id = null;
        info = new UserUpdate(id, info.username());
        info.validate();
        return switch (userService.updateUser(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws BindException {
        var info = new UserId(id);
        info.validate();
        return switch (userService.deleteUser(info)) {
            case Result.Ok<?, ServiceError> ignored -> responseNoContent();
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

}
