package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.service.data.OptionInfo;
import no.hvl.dat250.g13.project.service.data.PollInfo;
import no.hvl.dat250.g13.project.service.data.SurveyInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SurveyServiceTest {

    @Autowired
    private SurveyService surveyService;

    @MockBean
    private SurveyRepository surveyRepository;

    private static final OptionInfo optionEntry = new OptionInfo(Optional.empty(), Optional.empty(), Optional.of("option"), Optional.empty());
    private static final OptionInfo optionTextMissing = new OptionInfo(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

    private static final PollInfo pollEntry = new PollInfo(Optional.empty(), Optional.of("poll"), Optional.empty(), List.of(optionEntry));
    private static final PollInfo pollTextMissing = new PollInfo(Optional.empty(), Optional.empty(), Optional.empty(), List.of(optionEntry, optionEntry));
    private static final PollInfo pollOptionsMissing = new PollInfo(Optional.empty(), Optional.of("poll"), Optional.empty(), List.of());
    private static final PollInfo pollOptionTextMissing = new PollInfo(Optional.empty(), Optional.of("poll"), Optional.empty(), List.of(optionTextMissing));

    private static final SurveyInfo surveyEntry = new SurveyInfo(Optional.of(1L), Optional.of(""), Optional.of(List.of(pollEntry)), Optional.empty());
    private static final SurveyInfo surveyEntry1 = new SurveyInfo(Optional.empty(), Optional.of(""), Optional.of(List.of(pollEntry)), Optional.empty());
    private static final SurveyInfo surveyIdMissing = new SurveyInfo(Optional.empty(), Optional.of(""), Optional.of(List.of(pollEntry)), Optional.empty());
    private static final SurveyInfo surveyIdNotFound = new SurveyInfo(Optional.of(2L), Optional.of(""), Optional.of(List.of(pollEntry)), Optional.empty());
    private static final SurveyInfo surveyPollsMissing = new SurveyInfo(Optional.of(1L), Optional.of(""), Optional.empty(), Optional.empty());
    private static final SurveyInfo surveyPollTextMissing = new SurveyInfo(Optional.of(1L), Optional.of(""), Optional.of(List.of(pollTextMissing)), Optional.empty());
    private static final SurveyInfo surveyPollOptionsMissing = new SurveyInfo(Optional.of(1L), Optional.of(""), Optional.of(List.of(pollOptionsMissing)), Optional.empty());
    private static final SurveyInfo surveyPollOptionTextMissing = new SurveyInfo(Optional.of(1L), Optional.of(""), Optional.of(List.of(pollOptionTextMissing)), Optional.empty());

    @BeforeEach
    void setUp() {
        Mockito.when(surveyRepository.save(surveyEntry.into())).thenReturn(surveyEntry.into());
        Mockito.when(surveyRepository.save(surveyEntry1.into())).thenReturn(surveyEntry.into());

        Mockito.when(surveyRepository.findById(surveyEntry.id().get())).thenReturn(Optional.of(surveyEntry.into()));
        Mockito.when(surveyRepository.findById(surveyIdNotFound.id().get())).thenReturn(Optional.empty());

        Mockito.when(surveyRepository.existsById(1l)).thenReturn(true);
        Mockito.when(surveyRepository.existsById(2l)).thenReturn(false);

        Mockito.when(surveyRepository.findAll()).thenReturn(Streamable.empty());
    }

    @Test
    void createSurvey_ok() {
        var survey = surveyService.createSurvey(surveyEntry);
        assertTrue(survey.isOk());
    }

    @Test
    void createSurvey_pollsMissing() {
        var survey = surveyService.createSurvey(surveyPollsMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void createSurvey_pollTextMissing() {
        var survey = surveyService.createSurvey(surveyPollTextMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void createSurvey_pollOptionsMissing() {
        var survey = surveyService.createSurvey(surveyPollOptionsMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void createSurvey_pollOptionTextMissing() {
        var survey = surveyService.createSurvey(surveyPollOptionTextMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void updateSurvey_ok() {
        var survey = surveyService.updateSurvey(surveyEntry);
        assertTrue(survey.isOk());
    }

    @Test
    void updateSurvey_idMissing() {
        var survey = surveyService.updateSurvey(surveyIdMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void updateSurvey_idNotFound() {
        var survey = surveyService.updateSurvey(surveyIdNotFound);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.NOT_FOUND, survey.error().status());
    }

    @Test
    void readSurveyById_ok() {
        var survey = surveyService.readSurveyById(surveyEntry);
        assertTrue(survey.isOk());
    }

    @Test
    void readSurveyById_idMissing() {
        var survey = surveyService.readSurveyById(surveyIdMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void readSurveyById_idNotFound() {
        var survey = surveyService.readSurveyById(surveyIdNotFound);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.NOT_FOUND, survey.error().status());
    }

    @Test
    void readAllSurveys() {
        var surveys = surveyService.readAllSurveys();
        assertTrue(surveys.isOk());
    }

    @Test
    void deleteSurvey_ok() {
        var survey = surveyService.deleteSurvey(surveyEntry);
        assertTrue(survey.isOk());
        assertEquals(null, survey.value());
    }

    @Test
    void deleteSurvey_idMissing() {
        var survey = surveyService.deleteSurvey(surveyIdMissing);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.BAD_REQUEST, survey.error().status());
    }

    @Test
    void deleteSurvey() {
        var survey = surveyService.deleteSurvey(surveyIdNotFound);
        assertTrue(survey.isError());
        assertEquals(HttpStatus.NOT_FOUND, survey.error().status());
    }
}