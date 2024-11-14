package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.service.data.user.UserVotes;
import no.hvl.dat250.g13.project.service.data.vote.*;
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
        var info = new VoteCreate(1L, 1L, Set.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void create_userIdMissing() {
        var info = new VoteCreate(null, 1L, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_surveyIdMissing() {
        var info = new VoteCreate(1L,null, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_optionsMissing() {
        var info = new VoteCreate(1L, 1L, null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Selected options is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_ok() {
        var info = new VoteUpdate(1L, 1L, "REPLACE", Set.of());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void update_userIdMissing() {
        var info = new VoteUpdate(null, 1L, "REPLACE", Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_surveyIdMissing() {
        var info = new VoteUpdate(1L,null, "REPLACE", Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_replace() {
        var info = new VoteUpdate(1L,1L, "REPLACE", Set.of(1L));
        var votes = Set.of(new Vote(1L, 1L, 2L));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        assertEquals(Optional.of(1L), save.stream().findAny().map(Vote::getOptionId));
        assertEquals(Optional.of(2L), delete.stream().findAny().map(Vote::getOptionId));
    }

    @Test
    void update_union() {
        var info = new VoteUpdate(1L, 1L, "UNION", Set.of(1L));
        var votes = Set.of(new Vote(1L, 1L, 2L));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        assertEquals(Optional.of(1L), save.stream().findAny().map(Vote::getOptionId));
        assertEquals(Optional.empty(), delete.stream().findAny().map(Vote::getOptionId));
    }

    @Test
    void update_intersection() {
        var info = new VoteUpdate(1L, 1L, "INTERSECT", Set.of(1L));
        var votes = Set.of(new Vote(1L, 1L, 2L));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        assertEquals(Optional.empty(), save.stream().findAny().map(Vote::getOptionId));
        assertEquals(Optional.of(2L), delete.stream().findAny().map(Vote::getOptionId));
    }

    @Test
    void info_ok() {
        var info = new VoteInfo(1L, 1L, Set.of(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void info_userIdMissing() {
        var info = new VoteInfo(null, 1L, Set.of(1L));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_surveyIdMissing() {
        var info = new VoteInfo(1L, null, Set.of(1L));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void userVotes_ok() {
        var vote = new Vote(1L, 1L, 1L);
        var info = new UserVotes(vote.getUserId(), Set.of(vote));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }


    @Test
    void userVotes_userIdMissing() {
        var info = new UserVotes(null, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void surveyVotes_ok() {
        var vote = new Vote(1L,1L,1L);
        var info = new VoteSurveyVotes(vote.getSurveyId(), Set.of(vote));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void surveyVotes_surveyIdMissing() {
        var info = new VoteSurveyVotes(null, Set.of());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_optionsMissing() {
        var info = new VoteInfo(1L, 1L, (Iterable<Vote>) null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Selected options is required", violations.iterator().next().getMessage());
    }

    @Test
    void id_ok() {
        var info = new VoteId(1L, 1L);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_userIdMissing() {
        var info = new VoteId(null, 1L);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void id_surveyIdMissing() {
        var info = new VoteId(1L,null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }

    @Test
    void userId_ok() {
        var info = new VoteUserId(1L);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void userId_userIdMissing() {
        var info = new VoteUserId(null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void surveyId_ok() {
        var info = new VoteSurveyId(1L);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void surveyId_surveyIdMissing() {
        var info = new VoteSurveyId(null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Survey value is required", violations.iterator().next().getMessage());
    }
}