package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.survey.SurveyCreate;
import no.hvl.dat250.g13.project.service.data.survey.SurveyId;
import no.hvl.dat250.g13.project.service.data.survey.SurveyUpdate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private SurveyService surveyService;

    @Test
    void createSurvey_ok() {
        var create = new SurveyCreate("Survey", 1L, List.of());

        Mockito.when(surveyRepository.save(any())).then(returnsFirstArg());

        var result = surveyService.createSurvey(create);
        assertTrue(result.isOk());
        assertEquals(create.title(), result.value().title());
    }

    @Test
    void updateSurvey_ok() {
        var update = new SurveyUpdate(1L, Optional.empty(), List.of(), List.of(), List.of());
        var survey = new Survey();
        survey.setId(update.id());

        Mockito.when(surveyRepository.findById(survey.getId())).thenReturn(Optional.of(survey));
        Mockito.when(surveyRepository.save(any())).then(returnsFirstArg());

        var result = surveyService.updateSurvey(update);
        assertTrue(result.isOk());
    }

    @Test
    void updateSurvey_idNotFound() {
        var update = new SurveyUpdate(1L, Optional.empty(), List.of(), List.of(), List.of());
        var result = surveyService.updateSurvey(update);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readSurveyById_ok() {
        var key = new SurveyId(1L);
        var survey = new Survey();
        survey.setId(key.id());

        Mockito.when(surveyRepository.findById(key.id())).thenReturn(Optional.of(survey));

        var result = surveyService.readSurveyById(key);
        assertTrue(result.isOk());
        assertEquals(key.id(), result.value().id());
    }

    @Test
    void readSurveyById_idNotFound() {
        var key = new SurveyId(1L);
        var result = surveyService.readSurveyById(key);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readAllSurveys() {
        var surveys = surveyService.readAllSurveys();
        assertTrue(surveys.isOk());
    }

    @Test
    void deleteSurvey_ok() {
        var key = new SurveyId(1L);

        Mockito.when(surveyRepository.existsById(key.id())).thenReturn(true);

        var result = surveyService.deleteSurvey(key);
        assertTrue(result.isOk());
        assertNull(result.value());
    }

    @Test
    void deleteSurvey_notFound() {
        var key = new SurveyId(1L);
        var result = surveyService.deleteSurvey(key);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }
}