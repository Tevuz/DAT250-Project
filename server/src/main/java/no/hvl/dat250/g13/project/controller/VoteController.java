package no.hvl.dat250.g13.project.controller;

import jakarta.validation.Valid;
import no.hvl.dat250.g13.project.service.VoteService;
import no.hvl.dat250.g13.project.service.data.vote.VoteCreate;
import no.hvl.dat250.g13.project.service.data.vote.VoteId;
import no.hvl.dat250.g13.project.service.data.vote.VoteUpdate;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static no.hvl.dat250.g13.project.controller.Common.*;

@RestController
@RequestMapping("/api/votes")
@Validated
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping
    public ResponseEntity<?> createVote(@Valid @RequestBody VoteCreate info) {
        return switch (voteService.createVote(info)) {
            case Result.Ok<?, ServiceError> result -> responseCreated(result, URI.create(""));
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping("/{survey_id}/{user_id}")
    public ResponseEntity<?> readVote(@PathVariable Long survey_id, @PathVariable Long user_id) throws BindException {
        var info = new VoteId(user_id, survey_id);
        info.validate();
        return switch (voteService.readVoteById(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping
    public ResponseEntity<?> readAllVotes() {
        // TODO: implement pagination
        return switch (voteService.readAllVotes()) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @PutMapping()
    public ResponseEntity<?> updateVote(@Valid @RequestBody VoteUpdate info) {
        return switch (voteService.updateVote(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @DeleteMapping("/{survey_id}/{user_id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long survey_id, @PathVariable Long user_id) throws BindException {
        var info = new VoteId(user_id, survey_id);
        info.validate();
        return switch (voteService.deleteVote(info)) {
            case Result.Ok<?, ServiceError> ignored -> responseNoContent();
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

}
