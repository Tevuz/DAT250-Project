package no.hvl.dat250.g13.project.service.data.user;

import no.hvl.dat250.g13.project.service.data.validation.MutualExclusive;
import no.hvl.dat250.g13.project.service.data.validation.Validate;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.Optional;

/**
 *
 * @param id
 * @param username
 */
@MutualExclusive(fields = {"id", "username"}, message = "Exactly one of id or username must be provided")
public record UserId(
        Optional<Long> id,
        Optional<String> username
) implements Validate {
    public UserId(long id) {
        this(Optional.of(id), Optional.empty());
    }

    public UserId(String username) {
        this(Optional.empty(), Optional.of(username));
    }

    public static UserId parse(String identifier) throws BindException {
        if (identifier == null) {
            var exception = new BindException(new UserId(null, null), "UserId");
            exception.addError(new ObjectError("UserId", "Id or username is required"));
            throw exception;
        }

        System.out.println(identifier);

        try {
            if (identifier.startsWith("id:")) {
                return new UserId(Long.parseLong(identifier.substring(3)));
            }
            if (identifier.startsWith("id%3A")) {
                return new UserId(Long.parseLong(identifier.substring(5)));
            }
        } catch (NumberFormatException e) {
            var exception = new BindException(new UserId(null, null), "DTO");
            exception.addError(new FieldError("DTO", "id", "Id must be a number"));
            throw exception;
        }

        return new UserId(identifier);
    }
}
