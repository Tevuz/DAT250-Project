package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.survey.SurveyId;
import no.hvl.dat250.g13.project.service.data.user.UserId;
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
    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, SurveyRepository surveyRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
    }

    /**
     * Create a vote.
     *
     * @param info {@link VoteCreate} with data for the new vote
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if created</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#CONFLICT CONFLICT} if vote already exists</li>
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
     * @param info {@link VoteUpdate} with data to replace the vote with value=={@code info.value}
     * @return {@code Result<Void, ServiceError>}:
     *      <ul>
     *          <li>{@link Void} if updated</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if vote does not exist </li>
     *      </ul>
     */
    public Result<Void, ServiceError> updateVote(VoteUpdate info) {
        var votes = Streamable.of(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id())).toSet();
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        voteRepository.deleteAll(delete);
        voteRepository.saveAll(save);

        return new Result.Ok<>(null);
    }


    /**
     * Read a vote.
     *
     * @param info {@link VoteId} with user id and survey id for the vote to read
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if found</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if vote does not exist</li>
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
     * @param info {@link UserId} with user id for the votes to read
     * @return {@code Result<UserVotes, ServiceError>}:
     *      <ul>
     *          <li>{@link UserVotes}</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if user does not exist</li>
     *      </ul>
     */
    public Result<UserVotes, ServiceError> readVotesBy(UserId info) {
        var user_id = userRepository.findIdBy(info);
        if (user_id.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "User does not exist"));

        var votes = voteRepository.findAllByUserId(user_id.get());
        return new Result.Ok<>(new UserVotes(user_id.get(), votes));
    }

    /**
     * Read all votes registered to a survey.
     *
     * @param info {@link SurveyId} with survey value for the votes to read
     * @return {@code Result<VoteSurveyVotes, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteSurveyVotes}</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if survey does not exist</li>
     *      </ul>
     */
    public Result<VoteSurveyVotes, ServiceError> readVotesBySurveyId(SurveyId info) {
        if (!surveyRepository.existsById(info.id()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Survey does not exist"));

        var votes = voteRepository.findAllBySurveyId(info.id());
        return new Result.Ok<>(new VoteSurveyVotes(info.id(), votes));
    }

    /**
     * Read all votes.
     *
     * @return {@code Result<Iterable<VoteInfo>, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable}&lt;{@link VoteInfo}&gt;</li>
     *      </ul>
     */
    public Result<Iterable<VoteInfo>, ServiceError> readAllVotes() {
        var votes = voteRepository.findAll();
        return new Result.Ok<>(VoteInfo.from(votes));
    }

    /**
     * Delete a vote.
     *
     * @param info {@link VoteId} with user value and survey value for the vote to delete
     * @return {@code Result<Void, ServiceError>}:
     *      <ul>
     *          <li>{@link Void} if deleted</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with does not exist </li>
     *      </ul>
     */
    public Result<Void, ServiceError> deleteVote(VoteId info) {
        var votes = Streamable.of(voteRepository.findAllByUserIdAndSurveyId(info.user_id(), info.survey_id()));
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        voteRepository.deleteAll(votes);
        return new Result.Ok<>(null);
    }
}
