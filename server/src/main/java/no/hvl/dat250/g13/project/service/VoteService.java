package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.OptionRef;
import no.hvl.dat250.g13.project.domain.Vote;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.VoteInfo;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VoteService {

    private VoteRepository voteRepository;

    public VoteService(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    /**
     * Create a vote.
     *
     * @param info {@link VoteInfo} with data for the new vote
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if created</li>
     *          <li>{@link ServiceError} with {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing data </li>
     *          <li>{@link ServiceError} with {@link HttpStatus#CONFLICT CONFLICT} if the vote already exists </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> createVote(VoteInfo info) {
        if (info.user_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));
        if (info.survey_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));
        if (info.options().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Option selection was not provided"));

        if (voteRepository.existsById(info.id().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Vote already exists"));

        Vote vote = info.into();
        return new Result.Ok<>(new VoteInfo(voteRepository.save(vote)));
    }


    /**
     * Update a vote.
     *
     * @param info {@link VoteInfo} with new options to replace the vote with id=={@code info.id}
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing data</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> updateVote(VoteInfo info) {
        if (info.user_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));
        if (info.survey_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));
        if (info.options().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Option selection was not provided"));

        Optional<Vote> optional = voteRepository.findById(info.id().get());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        Vote vote = optional.get();
        vote.setOptions(info.options().stream().map(option -> new OptionRef(info.survey_id().get(), option)).toList());

        return new Result.Ok<>(new VoteInfo(voteRepository.save(vote)));
    }


    /**
     * Read a vote.
     *
     * @param info {@link VoteInfo} with user id and survey id for the vote to read
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id or survey id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> readVoteById(VoteInfo info) {
        if (info.user_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));
        if (info.survey_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));

        Optional<Vote> optional = voteRepository.findById(info.id().get());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        return new Result.Ok<>(new VoteInfo(optional.get()));
    }


    /**
     * Read all votes registered to a user.
     *
     * @param info {@link VoteInfo} with user id for the votes to read
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteInfo}</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id</li>
     *      </ul>
     */
    public Result<Iterable<VoteInfo>, ServiceError> readVotesByUserId(VoteInfo info) {
        if (info.user_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));

        var votes = voteRepository.findAllByIdVoterId(info.user_id().get());
        return new Result.Ok<>(Streamable.of(votes).map(VoteInfo::new).toList());
    }

    /**
     * Read all votes registered to a survey.
     *
     * @param info {@link VoteInfo} with survey id for the votes to read
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteInfo}</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing survey id</li>
     *      </ul>
     */
    public Result<Iterable<VoteInfo>, ServiceError> readVotesBySurveyId(VoteInfo info) {
        if (info.survey_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));

        var votes = voteRepository.findAllByIdSurveyId(info.survey_id().get());
        return new Result.Ok<>(Streamable.of(votes).map(VoteInfo::new));
    }

    /**
     * Read all votes.
     *
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link Iterable} of {@link VoteInfo}</li>
     *      </ul>
     */
    public Result<Iterable<VoteInfo>, ServiceError> readAllVotes() {
        var votes = voteRepository.findAll();
        return new Result.Ok<>(Streamable.of(votes).map(VoteInfo::new));
    }

    /**
     * Delete a vote.
     *
     * @param info {@link VoteInfo} with user id and survey id for the vote to delete
     * @return {@code Result<VoteInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link VoteInfo null} if deleted</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id or survey id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if vote with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<VoteInfo, ServiceError> deleteVote(VoteInfo info) {
        if (info.user_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));
        if (info.survey_id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "Survey id was not provided"));

        if (!voteRepository.existsById(info.id().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND, "Vote does not exist"));

        voteRepository.deleteById(info.id().get());
        return new Result.Ok<>(null);
    }
}
