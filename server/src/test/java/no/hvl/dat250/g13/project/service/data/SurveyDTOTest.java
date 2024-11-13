package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Identifiers.PollKey;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Option;
import no.hvl.dat250.g13.project.domain.Poll;
import no.hvl.dat250.g13.project.domain.Survey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SurveyDTOTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_ok() {
        var info = new SurveyDTO.Create("Survey", new UserKey(1L), default_pollCreate());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void create_titleMissing() {
        var info = new SurveyDTO.Create(null, new UserKey(1L), default_pollCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey title is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_titleEmpty() {
        var info = new SurveyDTO.Create("", new UserKey(1L), default_pollCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey title is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_titleBlank() {
        var info = new SurveyDTO.Create(" \n", new UserKey(1L), default_pollCreate());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey title is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_authorMissing() {
        {
            var info = new SurveyDTO.Create("Survey", null, default_pollCreate());
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Survey author value is required", violations.iterator().next().getMessage());
        }
        {
            var info = new SurveyDTO.Create("Survey", new UserKey(null), default_pollCreate());
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("must not be null", violations.iterator().next().getMessage());
        }
    }

    @Test
    void create_pollsEmpty() {
        var info = new SurveyDTO.Create("Survey", new UserKey(1L), List.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey polls is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_into() {
        var survey = new SurveyDTO.Create("Survey", new UserKey(1L), default_pollCreate()).into();
        assertEquals("Survey", survey.getTitle());
        assertEquals("Poll", survey.getPolls().getFirst().getText());
    }

    @Test
    void update_ok() {
        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.empty(),
                List.of(), List.of(), List.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void update_idMissing() {
        {
            var info = new SurveyDTO.Update(null, Optional.empty(),
                    List.of(), List.of(), List.of());
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Survey value is required", violations.iterator().next().getMessage());
        }
        {
            var info = new SurveyDTO.Update(new SurveyKey(null), Optional.empty(),
                    List.of(), List.of(), List.of());
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("must not be null", violations.iterator().next().getMessage());
        }
    }

    @Test
    void update_applyIdMismatch() {
        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(2L), Optional.empty(),
                List.of(), List.of(), List.of());
        assertThrows(IllegalArgumentException.class, () -> info.apply(survey), "Id mismatch");
    }

    @Test
    void update_applyNoChange() {
        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.empty(),
                List.of(), List.of(), List.of());
        info.apply(survey);
        assertEquals(default_survey(), survey);
    }

    @Test
    void update_applyTitleChange() {
        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.of("Changed"),
                List.of(), List.of(), List.of());
        info.apply(survey);
        assertEquals("Changed", survey.getTitle());
    }

    @Test
    void update_applyTitleEmpty() {
        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.of(""),
                List.of(), List.of(), List.of());
        info.apply(survey);
        assertEquals("Survey", survey.getTitle());
    }

    @Test
    void update_applyTitleBlank() {
        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.of(" \n"),
                List.of(), List.of(), List.of());
        info.apply(survey);
        assertEquals("Survey", survey.getTitle());
    }

    @Test
    void update_applyPollAdditions() {
        final var option = new OptionDTO.Create("Option", Optional.empty());
        final var pollAdd = new PollDTO.Create("Added", Optional.empty(), List.of(option));

        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.empty(),
                List.of(pollAdd), List.of(), List.of());
        info.apply(survey);
        assertEquals(3, survey.getPolls().size());
        assertEquals("Added", survey.getPolls().getLast().getText());
    }

    @Test
    void update_applyPollModifications() {
        final var pollUpdate = new PollDTO.Update(new PollKey(2L), Optional.of("Changed"), Optional.empty(), List.of(), List.of(), List.of());
        final var pollIgnore = new PollDTO.Update(new PollKey(9L), Optional.of("Ignored"), Optional.empty(), List.of(), List.of(), List.of());

        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.empty(),
                List.of(), List.of(pollUpdate, pollIgnore), List.of());
        info.apply(survey);
        assertEquals(2, survey.getPolls().size());
        assertEquals("Changed", survey.getPolls().get(1).getText());
        assertTrue(survey.getPolls().stream().noneMatch(poll -> 9L == poll.getId().value()));
    }

    @Test
    void update_applyPollDeletions() {
        final var pollDelete = new PollDTO.Id(new PollKey(1L));
        final var pollIgnore = new PollDTO.Id(new PollKey(9L));

        var survey = default_survey();

        var info = new SurveyDTO.Update(new SurveyKey(1L), Optional.empty(),
                List.of(), List.of(), List.of(pollDelete, pollIgnore));
        info.apply(survey);
        assertEquals(1, survey.getPolls().size());
        assertTrue(survey.getPolls().stream().noneMatch(option -> 1L == option.getId().value()));
        assertTrue(survey.getPolls().stream() .anyMatch(option -> 2L == option.getId().value()));
        assertTrue(survey.getPolls().stream().noneMatch(option -> 9L == option.getId().value()));
    }

    @Test
    void id_ok() {
        var info = new SurveyDTO.Id(new SurveyKey(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_idMissing() {
        {
            var info = new SurveyDTO.Id(null);
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Survey value is required", violations.iterator().next().getMessage());
        }
        {
            var info = new SurveyDTO.Id(new SurveyKey(null));
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("must not be null", violations.iterator().next().getMessage());
        }
    }

    Survey default_survey() {
        var option1 = new Option();
        option1.setId(new OptionKey(1L));
        option1.setText("Option1");
        option1.setIndex(0);

        var option2 = new Option();
        option2.setId(new OptionKey(2L));
        option2.setText("Option2");
        option2.setIndex(0);

        var poll1 = new Poll();
        poll1.setId(new PollKey(1L));
        poll1.setText("Poll1");
        poll1.setIndex(0);
        poll1.setOptions(List.of(option1));

        var poll2 = new Poll();
        poll2.setId(new PollKey(2L));
        poll2.setText("Poll2");
        poll2.setIndex(0);
        poll2.setOptions(List.of(option2));

        var survey = new Survey();
        survey.setId(new SurveyKey(1L));
        survey.setTitle("Survey");
        survey.setAuthor(new UserKey(1L));
        survey.setPolls(List.of(poll1, poll2));
        return survey;
    }

    List<OptionDTO.Create> default_optionCreate() {
        return List.of(new OptionDTO.Create("Option", Optional.empty()));
    }

    List<PollDTO.Create> default_pollCreate(){
        return List.of(new PollDTO.Create("Poll", Optional.empty(), default_optionCreate()));
    }

}