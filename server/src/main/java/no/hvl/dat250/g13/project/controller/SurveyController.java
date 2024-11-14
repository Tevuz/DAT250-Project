package no.hvl.dat250.g13.project.controller;

import jakarta.validation.Valid;
import no.hvl.dat250.g13.project.service.SurveyService;
import no.hvl.dat250.g13.project.service.data.survey.SurveyCreate;
import no.hvl.dat250.g13.project.service.data.survey.SurveyId;
import no.hvl.dat250.g13.project.service.data.survey.SurveyUpdate;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static no.hvl.dat250.g13.project.controller.Common.*;

@RestController
@RequestMapping("/api/surveys")
@Validated
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<?> createSurvey(@Valid @RequestBody SurveyCreate info) {
        return switch (surveyService.createSurvey(info)) {
            case Result.Ok<?, ServiceError> result -> responseCreated(result, URI.create(""));
            case Result.Error<?, ServiceError> result -> responseError(result);
        };

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readSurvey(@PathVariable Long id) throws BindException {
        var info = new SurveyId(id);
        info.validate();
        return switch (surveyService.readSurveyById(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @GetMapping
    public ResponseEntity<?> readAllSurveys() {
        // TODO: implement pagination
        return switch (surveyService.readAllSurveys()) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @PutMapping()
    public ResponseEntity<?> updateSurvey(@Valid @RequestBody SurveyUpdate info) {
        return switch (surveyService.updateSurvey(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody SurveyUpdate info) throws BindException {
        if (info.id() != null && !id.equals(info.id()))
            id = null;
        info = new SurveyUpdate(id, info.title(), info.poll_additions(), info.poll_modifications(), info.poll_deletions());
        info.validate();
        return switch (surveyService.updateSurvey(info)) {
            case Result.Ok<?, ServiceError> result -> responseOk(result);
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws BindException {
        var info = new SurveyId(id);
        info.validate();
        return switch (surveyService.deleteSurvey(info)) {
            case Result.Ok<?, ServiceError> ignored -> responseNoContent();
            case Result.Error<?, ServiceError> result -> responseError(result);
        };
    }

}
