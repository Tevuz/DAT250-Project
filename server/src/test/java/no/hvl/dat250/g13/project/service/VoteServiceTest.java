package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.VoteDTO;
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
        /*Mockito.when(voteRepository.save(infoExists.into())).thenReturn(infoExists.into());
        Mockito.when(voteRepository.save(infoEntry.into())).thenReturn(infoEntry.into());

        Mockito.when(voteRepository.findById(infoExists.id().get())).thenReturn(Optional.of(infoExists.into()));
        Mockito.when(voteRepository.findById(infoEntry.id().get())).thenReturn(Optional.of(infoEntry.into()));
        Mockito.when(voteRepository.findById(infoNotFound.id().get())).thenReturn(Optional.empty());

        Mockito.when(voteRepository.findAll()).thenReturn(Streamable.empty());

        Mockito.when(voteRepository.existsById(infoExists.id().get())).thenReturn(true);
        Mockito.when(voteRepository.existsById(infoEntry.id().get())).thenReturn(false);
        Mockito.when(voteRepository.existsById(infoNotFound.id().get())).thenReturn(false);*/

        Mockito.when(voteRepository.save(any())).then(returnsFirstArg());

        Mockito.when(voteRepository.findAllByUserId(new UserKey(2L))).thenReturn(Streamable.of(new Vote(new UserKey(2L), new SurveyKey(2L), new OptionKey(1L))));
        Mockito.when(voteRepository.findAllBySurveyId(new SurveyKey(2L))).thenReturn(Streamable.of(new Vote(new UserKey(2L), new SurveyKey(2L), new OptionKey(1L))));

        Mockito.when(voteRepository.existsBy(new UserKey(1L), new SurveyKey(1L))).thenReturn(false);
        Mockito.when(voteRepository.existsBy(new UserKey(2L), new SurveyKey(2L))).thenReturn(true);
        Mockito.when(voteRepository.existsBy(new UserKey(3L), new SurveyKey(3L))).thenReturn(false);

        Mockito.when(voteRepository.findAllBy(new UserKey(2L), new SurveyKey(2L))).thenReturn(Streamable.of(new Vote(new UserKey(2L), new SurveyKey(2L), new OptionKey(1L))));
        Mockito.when(voteRepository.findAllBy(new UserKey(3L), new SurveyKey(3L))).thenReturn(Streamable.of());
    }

    @Test
    void createVote_ok() {
        var info = new VoteDTO.Create(new UserKey(1L), new SurveyKey(1L), Set.of(new OptionKey(1L)));
        var vote = voteService.createVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void createVote_exists() {
        var info = new VoteDTO.Create(new UserKey(2L), new SurveyKey(2L), Set.of(new OptionKey(1L)));
        var vote = voteService.createVote(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.CONFLICT, vote.error().status());
    }

    @Test
    void updateVote_ok() {
        var info = new VoteDTO.Update(new UserKey(2L), new SurveyKey(2L), "REPLACE", Set.of(new OptionKey(1L)));
        var vote = voteService.updateVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void updateVote_idNotFound() {
        var info = new VoteDTO.Update(new UserKey(3L), new SurveyKey(3L), "REPLACE", Set.of(new OptionKey(1L)));
        var vote = voteService.updateVote(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVoteById_ok() {
        var info = new VoteDTO.Id(new UserKey(2L), new SurveyKey(2L));
        var vote = voteService.readVoteById(info);
        assertTrue(vote.isOk());
    }

    @Test
    void readVoteById_notFound() {
        var info = new VoteDTO.Id(new UserKey(3L), new SurveyKey(3L));
        var vote = voteService.readVoteById(info);
        assertTrue(vote.isError());
        assertEquals(HttpStatus.NOT_FOUND, vote.error().status());
    }

    @Test
    void readVotesByUserId_ok() {
        var info = new VoteDTO.UserId(new UserKey(2L));
        var vote = voteService.readVotesByUserId(info);
        assertTrue(vote.isOk());
    }

    @Test
    void readVotesBySurveyId_ok() {
        var info = new VoteDTO.SurveyId(new SurveyKey(2L));
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
        var info = new VoteDTO.Id(new UserKey(2L), new SurveyKey(2L));
        var vote = voteService.deleteVote(info);
        assertTrue(vote.isOk());
    }

    @Test
    void deleteVote_notFound() {
        var info = new VoteDTO.Id(new UserKey(1L), new SurveyKey(1L));
        var votes = voteService.deleteVote(info);
        assertTrue(votes.isError());
        assertEquals(HttpStatus.NOT_FOUND, votes.error().status());
    }
}