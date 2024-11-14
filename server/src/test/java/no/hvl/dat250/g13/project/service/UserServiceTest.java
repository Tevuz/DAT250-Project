package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.service.data.user.UserCreate;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.data.user.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setupMock() {
        Mockito.when(userRepository.save(any())).then(returnsFirstArg());

        Mockito.when(userRepository.existsByUsername("exists")).thenReturn(true);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.existsById(2L)).thenReturn(false);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity(1l, "username")));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());
    }

    @Test
    void createUser_ok() {
        var info = new UserCreate("user");
        var user = userService.createUser(info);
        assertTrue(user.isOk());
    }

    @Test
    void createUser_usernameTaken() {
        var info = new UserCreate("exists");
        var user = userService.createUser(info);
        assertTrue(user.isError());
        assertEquals(HttpStatus.CONFLICT, user.error().status());
    }

    @Test
    void updateUser_ok() {
        var info = new UserUpdate(1l, Optional.empty());
        var user = userService.updateUser(info);
        assertTrue(user.isOk());
    }

    @Test
    void updateUser_usernameChange() {
        var info = new UserUpdate(1l, Optional.of("changed"));
        var user = userService.updateUser(info);
        assertTrue(user.isOk());
        assertEquals("changed", user.value().username());
    }

    @Test
    void updateUser_idNotFound() {
        var info = new UserUpdate(2l, Optional.of("changed"));
        var user = userService.updateUser(info);
        assertTrue(user.isError());
        assertEquals(HttpStatus.NOT_FOUND, user.error().status());
    }

    @Test
    void updateUser_usernameTaken() {
        var info = new UserUpdate(1l, Optional.of("exists"));
        var user = userService.updateUser(info);
        assertTrue(user.isError());
        assertEquals(HttpStatus.CONFLICT, user.error().status());
    }

    @Test
    void readUserById_ok() {
        var info = new UserId(1l);
        var user = userService.readUserById(info);
        assertTrue(user.isOk());
    }

    @Test
    void readUserById_idNotFound() {
        var info = new UserId(2l);
        var user = userService.readUserById(info);
        assertTrue(user.isError());
        assertEquals(HttpStatus.NOT_FOUND, user.error().status());
    }

    @Test
    void readAllUsers() {
        var list = userService.readAllUsers();
        assertTrue(list.isOk());
    }

    @Test
    void deleteUser_ok() {
        var info = new UserId(1l);
        var user = userService.deleteUser(info);
        assertTrue(user.isOk());
        assertTrue(user.getOk().isEmpty());
    }

    @Test
    void deleteUser_idNotFound() {
        var info = new UserId(2l);
        var user = userService.deleteUser(info);
        assertTrue(user.isError());
        assertEquals(HttpStatus.NOT_FOUND, user.error().status());
    }
}