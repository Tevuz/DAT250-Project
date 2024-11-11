package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import no.hvl.dat250.g13.project.domain.UserEntity;

import java.util.Objects;
import java.util.Optional;

public abstract class UserDTO {

    // ---- User Constraints ----
    public static final int USERNAME_LENGTH_MIN = 3;
    public static final int USERNAME_LENGTH_MAX = 20;
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]*$";

    // ---- User Messages ----
    public static final String MESSAGE_USER_ID_REQUIRED = "User id is required";
    public static final String MESSAGE_USERNAME_REQUIRED = "Username is required";
    public static final String MESSAGE_USERNAME_TO_LONG = "Username is to long";
    public static final String MESSAGE_USERNAME_TO_SHORT = "Username is to short";
    public static final String MESSAGE_USERNAME_NOT_MATCHING_PATTERN = "Username can only contain: A-Z a-z 0-9 _ -";

    /**
     *
     * @param username
     */
    public record Create(
            @NotNull(message = MESSAGE_USERNAME_REQUIRED)
            @Size(min = USERNAME_LENGTH_MIN, message = MESSAGE_USERNAME_TO_SHORT)
            @Size(max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_TO_LONG)
            @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_NOT_MATCHING_PATTERN)
            String username
    ) {
        public Create(UserEntity entity) {
            this(entity.getUsername());
        }

        public UserEntity into() {
            var entity = new UserEntity();
            entity.setUsername(username);
            return entity;
        }
    }

    /**
     *
     * @param id
     * @param username
     */
    public record Update(
            @NotNull(message = MESSAGE_USER_ID_REQUIRED)
            Long id,
            Optional<
                @Size(min = USERNAME_LENGTH_MIN, message = MESSAGE_USERNAME_TO_SHORT)
                @Size(max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_TO_LONG)
                @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_NOT_MATCHING_PATTERN)
                String> username
    ) {
        public Update(UserEntity entity) {
            this(entity.getId(), Optional.ofNullable(entity.getUsername()));
        }

        public UserEntity modify(UserEntity entity) {
            if (!Objects.equals(id, entity.getId()))
                throw new IllegalArgumentException("id mismatch");
            username.ifPresent(entity::setUsername);
            return entity;
        }
    }

    /**
     *
     * @param id
     * @param username
     */
    public record Info(
            @NotNull(message = MESSAGE_USER_ID_REQUIRED)
            Long id,
            @NotNull(message = MESSAGE_USERNAME_REQUIRED)
            @Size(min = USERNAME_LENGTH_MIN, message = MESSAGE_USERNAME_TO_SHORT)
            @Size(max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_TO_LONG)
            @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_NOT_MATCHING_PATTERN)
            String username
    ) {
        public Info(UserEntity entity) {
            this(entity.getId(), entity.getUsername());
        }
    }

    /**
     *
     * @param id
     */
    public record Id(
            @NotNull(message = MESSAGE_USER_ID_REQUIRED)
            Long id
    ) {
        public Id(UserEntity entity) {
            this(entity.getId());
        }
    }

    /**
     *
     * @param username
     */
    public record Username(
            @NotNull(message = MESSAGE_USERNAME_REQUIRED)
            @Size(min = USERNAME_LENGTH_MIN, message = MESSAGE_USERNAME_TO_SHORT)
            @Size(max = USERNAME_LENGTH_MAX, message = MESSAGE_USERNAME_TO_LONG)
            @Pattern(regexp = USERNAME_PATTERN, message = MESSAGE_USERNAME_NOT_MATCHING_PATTERN)
            String username
    ) {
        public Username(UserEntity entity) {
            this(entity.getUsername());
        }
    }

}