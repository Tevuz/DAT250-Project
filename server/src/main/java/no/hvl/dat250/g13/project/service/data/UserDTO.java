package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import no.hvl.dat250.g13.project.domain.UserEntity;

import java.util.Objects;
import java.util.Optional;

import static no.hvl.dat250.g13.project.service.data.ValidationConstants.*;

public abstract class UserDTO {

    /**
     *
     * @param username
     */
    public record Create(
            @NotNull(message = "Username is required")
            @Size(min = USERNAME_LENGTH_MIN, message = "Username is to short")
            @Size(max = USERNAME_LENGTH_MAX, message = "Username is to long")
            @Pattern(regexp = USERNAME_PATTERN, message = "Username can only contain [ A-Z a-z 0-9 _ - ]")
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
            @NotNull(message = "Id is required")
            Long id,
            Optional<
                @Size(min = USERNAME_LENGTH_MIN, message = "Username is to short")
                @Size(max = USERNAME_LENGTH_MAX, message = "Username is to long")
                @Pattern(regexp = USERNAME_PATTERN)
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
            @NotNull(message = "Id is required")
            Long id,
            @NotNull(message = "Username is required")
            @Size(min = USERNAME_LENGTH_MIN, message = "Username is to short")
            @Size(max = USERNAME_LENGTH_MAX, message = "Username is to long")
            @Pattern(regexp = USERNAME_PATTERN)
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
            @NotNull(message = "Id is required")
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
            @NotNull(message = "Username is required")
            @Size(min = USERNAME_LENGTH_MIN, message = "Username is to short")
            @Size(max = USERNAME_LENGTH_MAX, message = "Username is to long")
            @Pattern(regexp = USERNAME_PATTERN)
            String username
    ) {
        public Username(UserEntity entity) {
            this(entity.getUsername());
        }
    }

}