package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.service.data.UserDTO;
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
     * @param info {@link UserDTO} with data for the new user
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserDTO.Info} if created</li>
     *          <li>{@link ServiceError} with {@link HttpStatus#CONFLICT CONFLICT} if the username is taken </li>
     *      </ul>
     */
    public Result<UserDTO.Info, ServiceError> createUser(UserDTO.Create info) {
        if (userRepository.existsByUsername(info.username()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Username is not available"));

        UserEntity user = info.into();
        return new Result.Ok<>(new UserDTO.Info(userRepository.save(user)));
    }

    /**
     * Update a user.
     *
     * @param info {@link UserDTO} with data to replace the user with id=={@code info.id}
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserDTO} if updated</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if user with {@code info.id} does not exist </li>
     *          <li>{@link ServiceError} with {@link HttpStatus#CONFLICT CONFLICT} if the username is taken </li>
     *      </ul>
     */
    public Result<UserDTO.Info, ServiceError> updateUser(UserDTO.Update info) {
        Optional<UserEntity> optional = userRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));
        UserEntity user = optional.get();

        if (info.username().isPresent() &&
            !info.username().get().equals(user.getUsername()) &&
            userRepository.existsByUsername(info.username().get()))
            return new Result.Error<>(new ServiceError(HttpStatus.CONFLICT, "Username is not available"));

        user = info.modify(user);

        return new Result.Ok<>(new UserDTO.Info(userRepository.save(user)));
    }

    /**
     * Read a user.
     *
     * @param info {@link UserDTO} with id for the user to read
     * @return {@code Result<UserInfo, ServiceError>}:
     *      <ul>
     *          <li>{@link UserDTO} if found</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#BAD_REQUEST BAD_REQUEST} if the request is missing user id </li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if user with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<UserDTO.Info, ServiceError> readUserById(UserDTO.Id info) {
        Optional<UserEntity> optional = userRepository.findById(info.id());
        if (optional.isEmpty())
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        return new Result.Ok<>(new UserDTO.Info(optional.get()));
    }

    /**
     * Read all users.
     *
     * @return {@code Result<Iterable<UserInfo>, ServiceError>}:
     *      <ul>
     *          <li>{@link UserDTO} list of all users</li>
     *      </ul>
     */
    public Result<Iterable<UserDTO.Info>, ServiceError> readAllUsers() {
        return new Result.Ok<>(Streamable.of(userRepository.findAll()).map(UserDTO.Info::new));
    }

    /**
     * Delete a user.
     *
     * @param info {@link UserDTO} with id for the user to delete
     * @return Result&lt;UserInfo, ServiceError&gt;
     *      <ul>
     *          <li>{@link UserDTO null} if deleted successfully</li>
     *          <li>{@link ServiceError} of {@link HttpStatus#NOT_FOUND NOT_FOUND} if user with {@code info.id} does not exist </li>
     *      </ul>
     */
    public Result<UserDTO.Info, ServiceError> deleteUser(UserDTO.Id info) {
        if (!userRepository.existsById(info.id()))
            return new Result.Error<>(new ServiceError(HttpStatus.NOT_FOUND,"User does not exist"));

        userRepository.deleteById(info.id());
        return new Result.Ok<>(null);
    }
}
