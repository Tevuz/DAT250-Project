package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.service.data.user.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserDTOTest {

    private static Validator validator;

    @BeforeAll
    static void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void create_usernameMissing() {
        var info = new UserCreate((String)null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_usernameMinLength() {
        {
            var info = new UserCreate("12");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserCreate("123");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void create_usernameMaxLength() {
        {
            var info = new UserCreate("12345678901234567890a");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserCreate("12345678901234567890");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void update_idMissing() {
        var info = new UserUpdate(null, Optional.of("username"));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_usernameOptional() {
        var info = new UserUpdate(1L, Optional.empty());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void update_usernameMinLength() {
        {
            var info = new UserUpdate(1L, Optional.of("12"));
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserUpdate(1L, Optional.of("123"));
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void update_usernameMaxLength() {
        {
            var info = new UserUpdate(1L, Optional.of("12345678901234567890a"));
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserUpdate(1L, Optional.of("12345678901234567890"));
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void info_idMissing() {
        var info = new UserInfo(null, "username");
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_usernameMissing() {
        var info = new UserInfo(1L, null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_usernameMinLength() {
        {
            var info = new UserInfo(1L, "12");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserInfo(1L, "123");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void info_usernameMaxLength() {
        {
            var info = new UserInfo(1L, "12345678901234567890a");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserInfo(1L, "12345678901234567890");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void id_idOk() {
        var info = new UserId(1L);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_usernameOk() {
        var info = new UserId("username");
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_noneProvided() {
        var info = new UserId(Optional.empty(), Optional.empty());
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Exactly one of id or username must be provided", violations.iterator().next().getMessage());
    }

    @Test
    void id_idAndUsernameProvided() {
        var info = new UserId(Optional.of(1L), Optional.of("username"));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Exactly one of id or username must be provided", violations.iterator().next().getMessage());
    }

    @Test
    void id_parseId() throws BindException {
        var info = UserId.parse("id:1");
        assertEquals(Optional.of(1L), info.id());
        assertEquals(Optional.empty(), info.username());
    }

    @Test
    void id_parseUsername() throws BindException {
        var info = UserId.parse("username");
        assertEquals(Optional.empty(), info.id());
        assertEquals(Optional.of("username"), info.username());
    }

    @Test
    void id_parseNull() {
        assertThrows(BindException.class, () -> UserId.parse(null), "Id or username is required");
    }

    @Test
    void id_parseIdNAN() {
        assertThrows(BindException.class, () -> UserId.parse("id:nan"), "Id must be a number");
    }
}
