package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.user.*;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private final UserRepository userRepository;
    private final SurveyRepository surveyRepository;
    private final VoteRepository voteRepository;

    public UserService(UserRepository userRepository, SurveyRepository surveyRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.surveyRepository = surveyRepository;
        this.voteRepository = voteRepository;
    }

    /**
     * Create a user.
     *
     * @param info {@link UserCreate} with data for the new user
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserInfo} if created</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#CONFLICT CONFLICT} if the username is taken</li>
     *      </ul>
     */
    public Result<UserInfo, ServiceError> createUser(UserCreate info) {
        if (userRepository.existsByUsername(info.username()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Username is not available"));

        UserEntity user = info.into();
        return new Result.Ok<>(new UserInfo(userRepository.save(user)));
    }

    /**
     * Update a user.
     *
     * @param info {@link UserUpdate} with data to replace the user with value=={@code info.value}
     * @return {@code Result<Void, ServiceError>}:
     *      <ul>
     *          <li>{@link Void} if updated</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if user does not exist</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#CONFLICT CONFLICT} if the username is taken</li>
     *      </ul>
     */
    public Result<Void, ServiceError> updateUser(UserUpdate info) {
        Optional<UserEntity> optional = userRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));
        UserEntity user = optional.get();

        if (info.username().isPresent() &&
            !info.username().get().equals(user.getUsername()) &&
            userRepository.existsByUsername(info.username().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Username is not available"));

        info.apply(user);
        userRepository.save(user);
        return new Result.Ok<>(null);
    }

    /**
     * Read a user by value.
     *
     * @param info {@link UserId} with id for the user to read
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserInfo} if found</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if user does not exist</li>
     *      </ul>
     */
    public Result<UserInfo, ServiceError> readUser(UserId info) {
        Optional<UserEntity> optional = userRepository.findBy(info);
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        return new Result.Ok<>(new UserInfo(optional.get()));
    }

    /**
     * Read a users votes.
     *
     * @param info {@link UserId} with id for the users votes to read
     * @return {@code Result<UserVotes, ServiceError>}:
     *      <ul>
     *          <li>{@link UserVotes} if found</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if user does not exist</li>
     *      </ul>
     */
    public Result<UserVotes, ServiceError> readUserVotes(UserId info) {
        Optional<UserEntity> optional = userRepository.findBy(info);
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));
        UserEntity user = optional.get();

        var votes = voteRepository.findAllByUserId(user.getId());
        return new Result.Ok<>(new UserVotes(user.getId(), votes));
    }

    /**
     * Read a users surveys.
     *
     * @param info {@link UserId} with id for the users surveys to read
     * @return {@code Result<UserSurveys, ServiceError>}:
     *      <ul>
     *          <li>{@link UserSurveys} if found</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if user does not exist</li>
     *      </ul>
     */
    public Result<UserSurveys, ServiceError> readUserSurveys(UserId info) {
        Optional<UserEntity> optional = userRepository.findBy(info);
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));
        UserEntity user = optional.get();

        var votes = surveyRepository.findAllByAuthorId(user.getId());
        return new Result.Ok<>(new UserSurveys(user.getId(), votes));
    }

    /**
     * Read all users.
     *
     * @return {@code Result<Iterable<UserInfo>, ServiceError>}:
     *      <ul>
     *          <li>{@link UserInfo} list of all users</li>
     *      </ul>
     */
    public Result<Iterable<UserInfo>, ServiceError> readAllUsers() {
        return new Result.Ok<>(Streamable.of(userRepository.findAll()).map(UserInfo::new).toList());
    }

    /**
     * Delete a user.
     *
     * @param info {@link UserInfo} with id for the user to delete
     * @return {@code Result<Void, ServiceError>}:
     *      <ul>
     *          <li>{@link Void} if deleted successfully</li>
     *          <li>{@link ServiceError} with {@code status=}{@link HttpStatus#NOT_FOUND NOT_FOUND} if user does not exist</li>
     *      </ul>
     */
    public Result<Void, ServiceError> deleteUser(UserId info) {
        if (!userRepository.existsBy(info))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        userRepository.deleteBy(info);
        return new Result.Ok<>(null);
    }
}
