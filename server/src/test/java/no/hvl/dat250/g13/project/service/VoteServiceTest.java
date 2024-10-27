package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.VoteInfo;
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
class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @MockBean
    private VoteRepository voteRepository;

    private static final VoteInfo infoUserIdEmpty = new VoteInfo(Optional.empty(), Optional.of(2L), List.of(1L));
    private static final VoteInfo infoSurveyIdEmpty = new VoteInfo(Optional.of(1L), Optional.empty(), List.of(1L));
    private static final VoteInfo infoOptionsEmpty = new VoteInfo(Optional.of(1L), Optional.of(2L), List.of());
    private static final VoteInfo infoExists = new VoteInfo(Optional.of(3L), Optional.of(3L), List.of(1L));
    private static final VoteInfo infoEntry = new VoteInfo(Optional.of(4L), Optional.of(4L), List.of(1L));
    private static final VoteInfo infoNotFound = new VoteInfo(Optional.of(5L), Optional.of(5L), List.of(1L));

    @BeforeEach
    void setupMock() {
        Mockito.when(voteRepository.save(infoExists.into())).thenReturn(infoExists.into());
        Mockito.when(voteRepository.save(infoEntry.into())).thenReturn(infoEntry.into());

        Mockito.when(voteRepository.findById(infoExists.id().get())).thenReturn(Optional.of(infoExists.into()));
        Mockito.when(voteRepository.findById(infoEntry.id().get())).thenReturn(Optional.of(infoEntry.into()));
        Mockito.when(voteRepository.findById(infoNotFound.id().get())).thenReturn(Optional.empty());

        Mockito.when(voteRepository.findAll()).thenReturn(Streamable.empty());

        Mockito.when(voteRepository.existsById(infoExists.id().get())).thenReturn(true);
        Mockito.when(voteRepository.existsById(infoEntry.id().get())).thenReturn(false);
        Mockito.when(voteRepository.existsById(infoNotFound.id().get())).thenReturn(false);
    }

    @Test
    void createVote_ok() {
        var vote = voteService.createVote(infoEntry);
        assertTrue(vote.isOk());
    }

    @Test
    void createVote_userIdNotProvided() {
        var vote = voteService.createVote(infoUserIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void createVote_surveyIdNotProvided() {
        var vote = voteService.createVote(infoSurveyIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void createVote_exists() {
        var vote = voteService.createVote(infoExists);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.CONFLICT, vote.error().status());
    }

    @Test
    void createVote_optionsEmpty() {
        var vote = voteService.createVote(infoOptionsEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void updateVote() {
        var vote = voteService.updateVote(infoExists);
        assertTrue(vote.isOk());
    }

    @Test
    void updateVote_userIdNotProvided() {
        var vote = voteService.updateVote(infoUserIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void updateVote_surveyIdNotProvided() {
        var vote = voteService.updateVote(infoSurveyIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void updateVote_optionsEmpty() {
        var vote = voteService.updateVote(infoOptionsEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void updateVote_idNotFound() {
        var vote = voteService.updateVote(infoNotFound);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVoteById_ok() {
        var vote = voteService.readVoteById(infoExists);
        assertTrue(vote.isOk());
    }

    @Test
    void readVoteById_userIdNotProvided() {
        var vote = voteService.readVoteById(infoUserIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void readVoteById_surveyIdNotProvided() {
        var vote = voteService.readVoteById(infoSurveyIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void readVoteById_notFound() {
        var vote = voteService.readVoteById(infoNotFound);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVotesByUserId_ok() {
        var vote = voteService.readVotesByUserId(infoExists);
        assertTrue(vote.isOk());
    }

    @Test
    void readVotesByUserId_userIdNotProvided() {
        var vote = voteService.readVotesByUserId(infoUserIdEmpty);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.BAD_REQUEST, vote.error().status());
    }

    @Test
    void readVotesBySurveyId_ok() {
        var votes = voteService.readVotesBySurveyId(infoExists);
        assertTrue(votes.isOk());
    }

    @Test
    void readVotesBySurveyId_surveyIdNotProvided() {
        var votes = voteService.readVotesBySurveyId(infoSurveyIdEmpty);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.BAD_REQUEST, votes.error().status());
    }

    @Test
    void readAllVotes() {
        var votes = voteService.readAllVotes();
        assertTrue(votes.isOk());
    }

    @Test
    void deleteVote_ok() {
        var vote = voteService.deleteVote(infoExists);
        assertTrue(vote.isOk());
    }

    @Test
    void deleteVote_userIdNotProvided() {
        var votes = voteService.deleteVote(infoUserIdEmpty);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.BAD_REQUEST, votes.error().status());
    }

    @Test
    void deleteVote_surveyIdNotProvided() {
        var votes = voteService.deleteVote(infoSurveyIdEmpty);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.BAD_REQUEST, votes.error().status());
    }

    @Test
    void deleteVote_notFound() {
        var votes = voteService.deleteVote(infoNotFound);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.NOT_FOUND, votes.error().status());
    }
}