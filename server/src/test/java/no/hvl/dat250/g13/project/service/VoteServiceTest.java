package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.vote.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @MockBean
    private VoteRepository voteRepository;

    @BeforeEach
    void setupMock() {
        Mockito.when(voteRepository.save(any())).then(returnsFirstArg());

        Mockito.when(voteRepository.findAllByUserId(2L)).thenReturn(Streamable.of(new Vote(2L, 2L, 1L)));
        Mockito.when(voteRepository.findAllBySurveyId(2L)).thenReturn(Streamable.of(new Vote(2L, 2L, 1L)));

        Mockito.when(voteRepository.existsByUserIdAndSurveyId(1L, 1L)).thenReturn(false);
        Mockito.when(voteRepository.existsByUserIdAndSurveyId(2L, 2L)).thenReturn(true);
        Mockito.when(voteRepository.existsByUserIdAndSurveyId(3L, 3L)).thenReturn(false);

        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(2L, 2L)).thenReturn(Streamable.of(new Vote(2L, 2L, 1L)));
        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(3L, 3L)).thenReturn(Streamable.of());
    }

    @Test
    void createVote_ok() {
        var info = new VoteCreate(1L, 1L, Set.of(1L));
        var vote = voteService.createVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void createVote_exists() {
        var info = new VoteCreate(2L, 2L, Set.of(1L));
        var vote = voteService.createVote(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.CONFLICT, vote.error().status());
    }

    @Test
    void updateVote_ok() {
        var info = new VoteUpdate(2L, 2L, "REPLACE", Set.of(1L));
        var vote = voteService.updateVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void updateVote_idNotFound() {
        var info = new VoteUpdate(3L, 3L, "REPLACE", Set.of(1L));
        var vote = voteService.updateVote(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVoteById_ok() {
        var info = new VoteId(2L, 2L);
        var vote = voteService.readVoteById(info);
        assertTrue(vote.isOk());
    }

    @Test
    void readVoteById_notFound() {
        var info = new VoteId(3L, 3L);
        var vote = voteService.readVoteById(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVotesByUserId_ok() {
        var info = new VoteUserId(2L);
        var vote = voteService.readVotesByUserId(info);
        assertTrue(vote.isOk());
    }

    @Test
    void readVotesBySurveyId_ok() {
        var info = new VoteSurveyId(2L);
        var votes = voteService.readVotesBySurveyId(info);
        assertTrue(votes.isOk());
    }

    @Test
    void readAllVotes() {
        var votes = voteService.readAllVotes();
        assertTrue(votes.isOk());
    }

    @Test
    void deleteVote_ok() {
        var info = new VoteId(2L, 2L);
        var vote = voteService.deleteVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void deleteVote_notFound() {
        var info = new VoteId(1L, 1L);
        var votes = voteService.deleteVote(info);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.NOT_FOUND, votes.error().status());
    }
}