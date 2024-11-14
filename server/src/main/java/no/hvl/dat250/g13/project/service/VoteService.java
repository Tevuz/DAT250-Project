package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.user.UserVotes;
import no.hvl.dat250.g13.project.service.data.vote.*;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class VoteService {

    private final VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    /**
     * Create a vote.
     *
     * @param info {@link VoteInfo} with data for the new vote
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if created</li>
     *          <li>{@link ServiceError} with {@link HttpStatus#CONFLICT CONFLICT} if the vote already exists </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> createVote(VoteCreate info) {
        if (voteRepository.existsByUserIdAndSurveyId(info.user_id(), info.survey_id()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Vote already exists"));

        Iterable<Vote> vote = info.into();
        return new Result.Ok<>(new VoteInfo(info.user_id(), info.survey_id(), voteRepository.saveAll(vote)));
    }


    /**
     * Update a vote.
     *
     * @param info {@link VoteInfo} with new options to replace the vote with value=={@code info.value}
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.value} does not exist </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> updateVote(VoteUpdate info) {
        var votes = Streamable.of(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id())).toSet();
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        voteRepository.deleteAll(delete);
        var result = voteRepository.saveAll(save);

        return new Result.Ok<>(new VoteInfo(info.user_id(), info.survey_id(), result));
    }


    /**
     * Read a vote.
     *
     * @param info {@link VoteInfo} with user value and survey value for the vote to read
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.value} does not exist </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> readVoteById(VoteId info) {
        var votes = Streamable.of(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id())).toSet();
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        return new Result.Ok<>(new VoteInfo(info.user_id(), info.survey_id(), votes));
    }


    /**
     * Read all votes registered to a user.
     *
     * @param info {@link VoteInfo} with user value for the votes to read
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteInfo}</li>
     *      </ul>
     */
    public Result<UserVotes, ServiceError> readVotesByUserId(VoteUserId info) {
        var votes = voteRepository.findAllByUserId(info.user_id());
        return new Result.Ok<>(new UserVotes(info.user_id(), votes));
    }

    /**
     * Read all votes registered to a survey.
     *
     * @param info {@link VoteInfo} with survey value for the votes to read
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteInfo}</li>
     *      </ul>
     */
    public Result<VoteSurveyVotes, ServiceError> readVotesBySurveyId(VoteSurveyId info) {
        var votes = voteRepository.findAllBySurveyId(info.survey_id());
        return new Result.Ok<>(new VoteSurveyVotes(info.survey_id(), votes));
    }

    /**
     * Read all votes.
     *
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteInfo}</li>
     *      </ul>
     */
    public Result<Iterable<VoteInfo>, ServiceError> readAllVotes() {
        var votes = voteRepository.findAll();
        return new Result.Ok<>(VoteInfo.from(votes));
    }

    /**
     * Delete a vote.
     *
     * @param info {@link VoteInfo} with user value and survey value for the vote to delete
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo null} if deleted</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.value} does not exist </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> deleteVote(VoteId info) {
        var votes = Streamable.of(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id()));
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        voteRepository.deleteAll(votes);
        return new Result.Ok<>(null);
    }
}
