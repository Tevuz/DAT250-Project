package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Survey;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.service.data.survey.SurveyCreate;
import no.hvl.dat250.g13.project.service.data.survey.SurveyId;
import no.hvl.dat250.g13.project.service.data.survey.SurveyUpdate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @MockBean
    private SurveyRepository surveyRepository;

    @Test
    void createSurvey_ok() {
        var create = new SurveyCreate("Survey", 1l, List.of());

        Mockito.when(surveyRepository.save(any())).then(returnsFirstArg());

        var result = surveyService.createSurvey(create);
        assertTrue(result.isOk());
    }

    @Test
    void updateSurvey_ok() {
        var update = new SurveyUpdate(1l, Optional.empty(), List.of(), List.of(), List.of());
        var survey = new Survey();
        survey.setId(update.id());
        survey.setPolls(List.of());

        Mockito.when(surveyRepository.existsById(any())).thenReturn(true);
        Mockito.when(surveyRepository.findById(any())).thenReturn(Optional.of(survey));
        Mockito.when(surveyRepository.save(any())).then(returnsFirstArg());

        var result = surveyService.updateSurvey(update);
        assertTrue(result.isOk());
    }

    @Test
    void updateSurvey_idNotFound() {
        var update = new SurveyUpdate(1l, Optional.empty(), List.of(), List.of(), List.of());

        Mockito.when(surveyRepository.existsById(any())).thenReturn(false);
        Mockito.when(surveyRepository.findById(any())).thenReturn(Optional.empty());
        Mockito.when(surveyRepository.save(any())).thenReturn(returnsFirstArg());

        var result = surveyService.updateSurvey(update);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readSurveyById_ok() {
        var key = new SurveyId(1l);
        var survey = new Survey();
        survey.setId(key.id());
        survey.setPolls(List.of());

        Mockito.when(surveyRepository.existsById(any())).thenReturn(true);
        Mockito.when(surveyRepository.findById(any())).thenReturn(Optional.of(survey));

        var result = surveyService.readSurveyById(key);
        assertTrue(result.isOk());
    }

    @Test
    void readSurveyById_idNotFound() {
        var key = new SurveyId(1l);

        Mockito.when(surveyRepository.existsById(any())).thenReturn(false);
        Mockito.when(surveyRepository.findById(any())).thenReturn(Optional.empty());

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
        var key = new SurveyId(1l);

        Mockito.when(surveyRepository.existsById(any())).thenReturn(true);

        var result = surveyService.deleteSurvey(key);
        assertTrue(result.isOk());
        assertNull(result.value());
    }

    @Test
    void deleteSurvey_notFound() {
        var key = new SurveyId(1l);

        Mockito.when(surveyRepository.existsById(any())).thenReturn(false);

        var result = surveyService.deleteSurvey(key);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }
}