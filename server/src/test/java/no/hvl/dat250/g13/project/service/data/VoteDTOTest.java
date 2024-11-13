package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Vote;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VoteDTOTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_ok() {
        var info = new VoteDTO.Create(new UserKey(1L), new SurveyKey(1L), Set.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void create_userIdMissing() {
        var info = new VoteDTO.Create(null, new SurveyKey(1L), Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_surveyIdMissing() {
        var info = new VoteDTO.Create(new UserKey(1L),null, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_optionsMissing() {
        var info = new VoteDTO.Create(new UserKey(1L), new SurveyKey(1L), null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Selected options is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_ok() {
        var info = new VoteDTO.Update(new UserKey(1L), new SurveyKey(1L), "REPLACE", Set.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void update_userIdMissing() {
        var info = new VoteDTO.Update(null, new SurveyKey(1L), "REPLACE", Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_surveyIdMissing() {
        var info = new VoteDTO.Update(new UserKey(1L),null, "REPLACE", Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_replace() {
        var info = new VoteDTO.Update(new UserKey(1L),new SurveyKey(1L), "REPLACE", Set.of(new OptionKey(1L)));
        var votes = Set.of(new Vote(new UserKey(1L), new SurveyKey(1L), new OptionKey(2L)));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        assertEquals(Optional.of(new OptionKey(1L)), save.stream().findAny().map(Vote::getOptionId));
        assertEquals(Optional.of(new OptionKey(2L)), delete.stream().findAny().map(Vote::getOptionId));
    }

    @Test
    void update_union() {
        var info = new VoteDTO.Update(new UserKey(1L),new SurveyKey(1L), "UNION", Set.of(new OptionKey(1L)));
        var votes = Set.of(new Vote(new UserKey(1L), new SurveyKey(1L), new OptionKey(2L)));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        assertEquals(Optional.of(new OptionKey(1L)), save.stream().findAny().map(Vote::getOptionId));
        assertEquals(Optional.empty(), delete.stream().findAny().map(Vote::getOptionId));
    }

    @Test
    void update_intersection() {
        var info = new VoteDTO.Update(new UserKey(1L),new SurveyKey(1L), "INTERSECT", Set.of(new OptionKey(1L)));
        var votes = Set.of(new Vote(new UserKey(1L), new SurveyKey(1L), new OptionKey(2L)));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        assertEquals(Optional.empty(), save.stream().findAny().map(Vote::getOptionId));
        assertEquals(Optional.of(new OptionKey(2L)), delete.stream().findAny().map(Vote::getOptionId));
    }

    @Test
    void info_ok() {
        var info = new VoteDTO.Info(new UserKey(1L), new SurveyKey(1L), Set.of(new OptionKey(1L)));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void info_userIdMissing() {
        var info = new VoteDTO.Info(null, new SurveyKey(1L), Set.of(new OptionKey(1L)));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_surveyIdMissing() {
        var info = new VoteDTO.Info(new UserKey(1L),null, Set.of(new OptionKey(1L)));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void userVotes_ok() {
        var vote = new Vote(new UserKey(1L), new SurveyKey(1L), new OptionKey(1L));
        var info = new VoteDTO.UserVotes(vote.getUserId(), Set.of(vote));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void userVotes_userIdMissing() {
        var info = new VoteDTO.UserVotes(null, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void surveyVotes_ok() {
        var vote = new Vote(new UserKey(1L), new SurveyKey(1L), new OptionKey(1L));
        var info = new VoteDTO.SurveyVotes(vote.getSurveyId(), Set.of(vote));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void surveyVotes_surveyIdMissing() {
        var info = new VoteDTO.SurveyVotes(null, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_optionsMissing() {
        var info = new VoteDTO.Info(new UserKey(1L), new SurveyKey(1L), (Set<OptionKey>)null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Selected options is required", violations.iterator().next().getMessage());
    }

    @Test
    void id_ok() {
        var info = new VoteDTO.Id(new UserKey(1L), new SurveyKey(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_userIdMissing() {
        var info = new VoteDTO.Id(null, new SurveyKey(1L));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void id_surveyIdMissing() {
        var info = new VoteDTO.Id(new UserKey(1L),null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void userId_ok() {
        var info = new VoteDTO.UserId(new UserKey(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void userId_userIdMissing() {
        var info = new VoteDTO.UserId(null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void surveyId_ok() {
        var info = new VoteDTO.SurveyId(new SurveyKey(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void surveyId_surveyIdMissing() {
        var info = new VoteDTO.SurveyId(null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }
}