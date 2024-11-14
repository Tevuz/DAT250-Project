package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.service.data.user.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void id_ok() {
        var info = new UserId(1L);
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_idMissing() {
        var info = new UserId((Long) null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void username_usernameMissing() {
        var info = new UserUsername((String) null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void username_usernameMinLength() {
        {
            var info = new UserUsername("12");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserUsername("123");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void username_usernameMaxLength() {
        {
            var info = new UserUsername("12345678901234567890a");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username must be between 3 and 20 characters long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserUsername("12345678901234567890");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }
}
