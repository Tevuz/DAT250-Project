package no.hvl.dat250.g13.project.service.data;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
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
        var info = new UserDTO.Create((String)null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void create_usernameMinLength() {
        {
            var info = new UserDTO.Create("12");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to short", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Create("123");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void create_usernameMaxLength() {
        {
            var info = new UserDTO.Create("12345678901234567890a");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Create("12345678901234567890");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void update_idMissing() {
        var info = new UserDTO.Update(null, Optional.of("username"));
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void update_usernameOptional() {
        var info = new UserDTO.Update(new UserKey(1L), Optional.empty());
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void update_usernameMinLength() {
        {
            var info = new UserDTO.Update(new UserKey(1L), Optional.of("12"));
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to short", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Update(new UserKey(1L), Optional.of("123"));
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void update_usernameMaxLength() {
        {
            var info = new UserDTO.Update(new UserKey(1L), Optional.of("12345678901234567890a"));
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Update(new UserKey(1L), Optional.of("12345678901234567890"));
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void info_idMissing() {
        var info = new UserDTO.Info(null, "username");
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_usernameMissing() {
        var info = new UserDTO.Info(new UserKey(1L), null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void info_usernameMinLength() {
        {
            var info = new UserDTO.Info(new UserKey(1L), "12");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to short", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Info(new UserKey(1L), "123");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void info_usernameMaxLength() {
        {
            var info = new UserDTO.Info(new UserKey(1L), "12345678901234567890a");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Info(new UserKey(1L), "12345678901234567890");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void id_ok() {
        var info = new UserDTO.Id(new UserKey(1L));
        var violations = validator.validate(info);
        assertEquals(0, violations.size());
    }

    @Test
    void id_idMissing() {
        var info = new UserDTO.Id((UserKey) null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("User value is required", violations.iterator().next().getMessage());
    }

    @Test
    void username_usernameMissing() {
        var info = new UserDTO.Username((String)null);
        var violations = validator.validate(info);
        assertEquals(1, violations.size());
        assertEquals("Username is required", violations.iterator().next().getMessage());
    }

    @Test
    void username_usernameMinLength() {
        {
            var info = new UserDTO.Username("12");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to short", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Username("123");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }

    @Test
    void username_usernameMaxLength() {
        {
            var info = new UserDTO.Username("12345678901234567890a");
            var violations = validator.validate(info);
            assertEquals(1, violations.size());
            assertEquals("Username is to long", violations.iterator().next().getMessage());
        }
        {
            var info = new UserDTO.Username("12345678901234567890");
            var violations = validator.validate(info);
            assertEquals(0, violations.size());
        }
    }
}
