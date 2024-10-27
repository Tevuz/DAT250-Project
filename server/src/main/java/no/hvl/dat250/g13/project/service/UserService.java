package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.service.data.UserInfo;
import no.hvl.dat250.g13.project.service.error.ServiceError;
import no.hvl.dat250.g13.project.util.Result;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Create a user.
     *
     * @param info {@link UserInfo} with data for the new user
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserInfo} if created</li>
     *          <li>{@link ServiceError} with {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing data </li>
     *          <li>{@link ServiceError} with {@link HttpStatus#CONFLICT CONFLICT} if the username is taken </li>
     *      </ul>
     */
    public Result<UserInfo, ServiceError> createUser(UserInfo info) {
        if (info.username().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST,"Username was not provided"));

        if (userRepository.existsByUsername(info.username().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Username is not available"));

        UserEntity user = info.into();
        return new Result.Ok<>(new UserInfo(userRepository.save(user)));
    }

    /**
     * Update a user.
     *
     * @param info {@link UserInfo} with data to replace the user with id=={@code info.id}
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserInfo} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if user with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<UserInfo, ServiceError> updateUser(UserInfo info) {
        if (info.id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));

        Optional<UserEntity> optional = userRepository.findById(info.id().get());

        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        UserEntity user = optional.get();
        info.username().ifPresent(user::setUsername);

        return new Result.Ok<>(new UserInfo(userRepository.save(user)));
    }

    /**
     * Read a user.
     *
     * @param info {@link UserInfo} with id for the user to read
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserInfo} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if user with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<UserInfo, ServiceError> readUserById(UserInfo info) {
        if (info.id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));
        Optional<UserEntity> optional = userRepository.findById(info.id().get());

        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        return new Result.Ok<>(new UserInfo(optional.get()));
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
        return new Result.Ok<>(Streamable.of(userRepository.findAll()).map(UserInfo::new));
    }

    /**
     * Delete a user.
     *
     * @param info {@link UserInfo} with id for the user to delete
     * @return Result&lt;UserInfo, ServiceError&gt;
     *      <ul>
     *          <li>{@link UserInfo null} if deleted successfully</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if user with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<UserInfo, ServiceError> deleteUser(UserInfo info) {
        if (info.id().isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.BAD_REQUEST, "User id was not provided"));
        if (!userRepository.existsById(info.id().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        userRepository.deleteById(info.id().get());
        return new Result.Ok<>(null);
    }
}
