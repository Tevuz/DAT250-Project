package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.VoteDTO;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class VoteService {

    private VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    /**
     * Create a vote.
     *
     * @param info {@link VoteDTO.Info} with data for the new vote
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteDTO.Info} if created</li>
     *          <li>{@link ServiceError} with {@link HttpStatus#CONFLICT CONFLICT} if the vote already exists </li>
     *      </ul>
     */
    public Result<VoteDTO.Info, ServiceError> createVote(VoteDTO.Create info) {
        if (voteRepository.existsBy(info.user_id(), info.survey_id()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Vote already exists"));

        Iterable<Vote> vote = info.into();
        return new Result.Ok<>(new VoteDTO.Info(info.user_id(), info.survey_id(), voteRepository.saveAll(vote)));
    }


    /**
     * Update a vote.
     *
     * @param info {@link VoteDTO.Info} with new options to replace the vote with id=={@code info.id}
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteDTO.Info} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<VoteDTO.Info, ServiceError> updateVote(VoteDTO.Update info) {
        var votes = Streamable.of(voteRepository.findAllBy(info.user_id(), info.survey_id())).toSet();
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        var save = new HashSet<Vote>();
        var delete = new HashSet<Vote>();

        info.apply(votes, save, delete);

        voteRepository.deleteAll(delete);
        var result = voteRepository.saveAll(save);

        return new Result.Ok<>(new VoteDTO.Info(info.user_id(), info.survey_id(), result));
    }


    /**
     * Read a vote.
     *
     * @param info {@link VoteDTO.Info} with user id and survey id for the vote to read
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteDTO.Info} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<VoteDTO.Info, ServiceError> readVoteById(VoteDTO.Id info) {
        var votes = Streamable.of(voteRepository.findAllBy(info.user_id(), info.survey_id())).toSet();
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        return new Result.Ok<>(new VoteDTO.Info(info.user_id(), info.survey_id(), votes));
    }


    /**
     * Read all votes registered to a user.
     *
     * @param info {@link VoteDTO.Info} with user id for the votes to read
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteDTO.Info}</li>
     *      </ul>
     */
    public Result<VoteDTO.UserVotes, ServiceError> readVotesByUserId(VoteDTO.UserId info) {
        var votes = voteRepository.findAllByUserId(info.user_id());
        return new Result.Ok<>(new VoteDTO.UserVotes(info.user_id(), votes));
    }

    /**
     * Read all votes registered to a survey.
     *
     * @param info {@link VoteDTO.Info} with survey id for the votes to read
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteDTO.Info}</li>
     *      </ul>
     */
    public Result<VoteDTO.SurveyVotes, ServiceError> readVotesBySurveyId(VoteDTO.SurveyId info) {
        var votes = voteRepository.findAllBySurveyId(info.survey_id());
        return new Result.Ok<>(new VoteDTO.SurveyVotes(info.survey_id(), votes));
    }

    /**
     * Read all votes.
     *
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteDTO.Info}</li>
     *      </ul>
     */
    public Result<Iterable<VoteDTO.Info>, ServiceError> readAllVotes() {
        var votes = voteRepository.findAll();
        return new Result.Ok<>(VoteDTO.Info.from(votes));
    }

    /**
     * Delete a vote.
     *
     * @param info {@link VoteDTO.Info} with user id and survey id for the vote to delete
     * @return {@code Result<VoteDTO.Info, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteDTO.Info null} if deleted</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<VoteDTO.Info, ServiceError> deleteVote(VoteDTO.Id info) {
        var votes = Streamable.of(voteRepository.findAllBy(info.user_id(), info.survey_id()));
        if (votes.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        voteRepository.deleteAll(votes);
        return new Result.Ok<>(null);
    }
}
