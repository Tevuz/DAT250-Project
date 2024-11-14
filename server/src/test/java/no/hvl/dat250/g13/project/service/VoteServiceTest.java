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

        Mockito.when(voteRepository.findAllByUserId(2l)).thenReturn(Streamable.of(new Vote(2l, 2l, 1l)));
        Mockito.when(voteRepository.findAllBySurveyId(2l)).thenReturn(Streamable.of(new Vote(2l, 2l, 1l)));

        Mockito.when(voteRepository.existsByUserIdAndSurveyId(1l, 1l)).thenReturn(false);
        Mockito.when(voteRepository.existsByUserIdAndSurveyId(2l, 2l)).thenReturn(true);
        Mockito.when(voteRepository.existsByUserIdAndSurveyId(3l, 3l)).thenReturn(false);

        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(2l, 2l)).thenReturn(Streamable.of(new Vote(2l, 2l, 1l)));
        Mockito.when(voteRepository.findAllByUserIdAndSurveyId(3l, 3l)).thenReturn(Streamable.of());
    }

    @Test
    void createVote_ok() {
        var info = new VoteCreate(1l, 1l, Set.of(1l));
        var vote = voteService.createVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void createVote_exists() {
        var info = new VoteCreate(2l, 2l, Set.of(1l));
        var vote = voteService.createVote(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.CONFLICT, vote.error().status());
    }

    @Test
    void updateVote_ok() {
        var info = new VoteUpdate(2l, 2l, "REPLACE", Set.of(1l));
        var vote = voteService.updateVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void updateVote_idNotFound() {
        var info = new VoteUpdate(3l, 3l, "REPLACE", Set.of(1l));
        var vote = voteService.updateVote(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVoteById_ok() {
        var info = new VoteId(2l, 2l);
        var vote = voteService.readVoteById(info);
        assertTrue(vote.isOk());
    }

    @Test
    void readVoteById_notFound() {
        var info = new VoteId(3l, 3l);
        var vote = voteService.readVoteById(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVotesByUserId_ok() {
        var info = new VoteUserId(2l);
        var vote = voteService.readVotesByUserId(info);
        assertTrue(vote.isOk());
    }

    @Test
    void readVotesBySurveyId_ok() {
        var info = new VoteSurveyId(2l);
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
        var info = new VoteId(2l, 2l);
        var vote = voteService.deleteVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void deleteVote_notFound() {
        var info = new VoteId(1l, 1l);
        var votes = voteService.deleteVote(info);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.NOT_FOUND, votes.error().status());
    }
}