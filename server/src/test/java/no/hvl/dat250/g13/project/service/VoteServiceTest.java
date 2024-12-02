package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.survey.SurveyId;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.data.vote.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    void createVote_ok() {
        var info = new VoteCreate(1L, 1L, List.of(1L));

        Mockito.when(voteRepository.saveAll(any())).then(returnsFirstArg());

        var result = voteService.createVote(info);
        assertTrue(result.isOk());
        assertEquals(info.user_id(), result.value().user_id());
        assertEquals(info.survey_id(), result.value().survey_id());
        assertEquals(Set.copyOf(info.options()), result.value().options());
    }

    @Test
    void createVote_exists() {
        var info = new VoteCreate(1L, 1L, List.of(1L));

        Mockito.when(voteRepository.existsByUserIdAndSurveyId(1L, 1L)).thenReturn(true);

        var result = voteService.createVote(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.CONFLICT, result.error().status());
    }

    @Test
    void updateVote_ok() {
        var info = new VoteUpdate(1L, 1L, "REPLACE", Set.of(1L));
        var vote = new Vote(info.user_id(), info.survey_id(), 1L);

        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id())).thenReturn(List.of(vote));
        Mockito.when(voteRepository.saveAll(any())).then(returnsFirstArg());

        var result = voteService.updateVote(info);
        assertTrue(result.isOk());
    }

    @Test
    void updateVote_idNotFound() {
        var info = new VoteUpdate(1L, 1L, "REPLACE", Set.of(1L));
        var result = voteService.updateVote(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readVoteById_ok() {
        var info = new VoteId(1L, 1L);
        var vote = new Vote(info.user_id(), info.survey_id(), 1L);

        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id())).thenReturn(List.of(vote));

        var result = voteService.readVoteById(info);
        assertTrue(result.isOk());
        assertEquals(info.user_id(), result.value().user_id());
        assertEquals(info.survey_id(), result.value().survey_id());
        assertEquals(Set.of(vote.getOptionId()), result.value().options());
    }

    @Test
    void readVoteById_notFound() {
        var info = new VoteId(1L, 1L);
        var result = voteService.readVoteById(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readVotesByUserId_ok() {
        var info = new UserId(1L);

        Mockito.when(userRepository.findIdBy(info)).thenReturn(info.id());

        var result = voteService.readVotesBy(info);
        assertTrue(result.isOk());
    }

    @Test
    void readVotesBySurveyId_ok() {
        var info = new SurveyId(1L);

        Mockito.when(surveyRepository.existsById(info.id())).thenReturn(true);

        var results = voteService.readVotesBySurveyId(info);
        assertTrue(results.isOk());
    }

    @Test
    void readAllVotes() {
        var results = voteService.readAllVotes();
        assertTrue(results.isOk());
    }

    @Test
    void deleteVote_ok() {
        var info = new VoteId(1L, 1L);
        var vote = new Vote(info.user_id(), info.survey_id(), 1L);

        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id())).thenReturn(List.of(vote));

        var result = voteService.deleteVote(info);
        assertTrue(result.isOk());
        assertNull(result.value());
    }

    @Test
    void deleteVote_notFound() {
        var info = new VoteId(1L, 1L);
        var result = voteService.deleteVote(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }
}