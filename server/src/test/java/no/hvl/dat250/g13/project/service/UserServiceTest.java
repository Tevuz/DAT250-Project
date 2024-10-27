package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.service.data.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private static final UserInfo infoEmpty = new UserInfo(Optional.empty(), Optional.empty());
    private static final UserInfo infoUsername = new UserInfo(Optional.empty(), Optional.of("username"));
    private static final UserInfo infoUsername1 = new UserInfo(Optional.of(1L), Optional.of("username"));
    private static final UserInfo infoExists = new UserInfo(Optional.empty(), Optional.of("exists"));
    private static final UserInfo infoExists1 = new UserInfo(Optional.of(1L), Optional.of("exists"));
    private static final UserInfo infoChanged = new UserInfo(Optional.of(1L), Optional.of("changed"));
    private static final UserInfo infoNotFound = new UserInfo(Optional.of(2L), Optional.empty());

    @BeforeEach
    void setupMock() {
        Mockito.when(userRepository.save((infoUsername.into()))).thenReturn(new UserEntity(1L, "username"));
        Mockito.when(userRepository.save((infoUsername1.into()))).thenReturn(new UserEntity(1L, "username"));
        Mockito.when(userRepository.save(infoChanged.into())).thenReturn(new UserEntity(1L, "changed"));

        Mockito.when(userRepository.existsByUsername(infoExists.username().get())).thenReturn(true);
        Mockito.when(userRepository.existsByUsername(infoChanged.username().get())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(infoUsername.username().get())).thenReturn(false);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity(1L, "username")));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Mockito.when(userRepository.findAll()).thenReturn(Streamable.empty());
    }

    @Test
    void createUser_ok() {
        var user = userService.createUser(infoUsername);
        assertTrue(user.isOk());
    }

    @Test
    void createUser_usernameNotProvided() {
        var user = userService.createUser(infoEmpty);
        assertTrue(user.isError());
        assertEquals(HttpStatus.BAD_REQUEST, user.getError().get().status());
    }

    @Test
    void createUser_usernameTaken() {
        var user = userService.createUser(infoExists);
        assertTrue(user.isError());
        assertEquals(HttpStatus.CONFLICT, user.getError().get().status());
    }

    @Test
    void updateUser_usernameChange() {
        var user = userService.updateUser(infoChanged);
        assertTrue(user.isOk());
    }

    @Test
    void updateUser_idNotProvided() {
        var user = userService.updateUser(infoUsername);
        assertTrue(user.isError());
        assertEquals(HttpStatus.BAD_REQUEST, user.getError().get().status());
    }

    @Test
    void updateUser_idNotFound() {
        var user = userService.updateUser(infoNotFound);
        assertTrue(user.isError());
        assertEquals(HttpStatus.NOT_FOUND, user.getError().get().status());
    }

    @Test
    void updateUser_usernameTaken() {
        var user = userService.updateUser(infoExists1);
        assertTrue(user.isError());
        assertEquals(HttpStatus.CONFLICT, user.getError().get().status());
    }

    @Test
    void updateUser_usernameUnchanged() {
        var user = userService.updateUser(infoUsername1);
        assertTrue(user.isOk());
    }

    @Test
    void readUserById_ok() {
        var user = userService.readUserById(infoUsername1);
        assertTrue(user.isOk());
    }

    @Test
    void readUserById_idNotProvided() {
        var user = userService.readUserById(infoEmpty);
        assertTrue(user.isError());
        assertEquals(HttpStatus.BAD_REQUEST, user.getError().get().status());
    }

    @Test
    void readUserById_idNotFound() {
        var user = userService.readUserById(infoNotFound);
        assertTrue(user.isError());
        assertEquals(HttpStatus.NOT_FOUND, user.getError().get().status());
    }

    @Test
    void readAllUsers() {
        var list = userService.readAllUsers();
        assertTrue(list.isOk());
    }

    @Test
    void deleteUser_ok() {
        var user = userService.deleteUser(infoExists1);
        assertTrue(user.isOk());
        assertTrue(user.getOk().isEmpty());
    }

    @Test
    void deleteUser_idNotProvided() {
        var user = userService.deleteUser(infoEmpty);
        assertTrue(user.isError());
        assertEquals(HttpStatus.BAD_REQUEST, user.getError().get().status());
    }

    @Test
    void deleteUser_idNotFound() {
        var user = userService.deleteUser(infoNotFound);
        assertTrue(user.isError());
        assertEquals(HttpStatus.NOT_FOUND, user.getError().get().status());
    }
}