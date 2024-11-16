package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.user.UserCreate;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.data.user.UserUpdate;
import no.hvl.dat250.g13.project.service.data.user.UserUsername;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SurveyRepository surveyRepository;

    @Mock
    private VoteRepository voteRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ok() {
        var info = new UserCreate("user");

        Mockito.when(userRepository.save(any())).then(returnsFirstArg());

        var result = userService.createUser(info);
        assertTrue(result.isOk());
        assertEquals(info.username(), result.value().username());
    }

    @Test
    void createUser_usernameTaken() {
        var info = new UserCreate("exists");

        Mockito.when(userRepository.existsByUsername(info.username())).thenReturn(true);

        var result = userService.createUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.CONFLICT, result.error().status());
    }

    @Test
    void updateUser_ok() {
        var info = new UserUpdate(1L, Optional.empty());
        var user = new UserEntity();
        user.setId(info.id());

        Mockito.when(userRepository.findById(info.id())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).then(returnsFirstArg());

        var result = userService.updateUser(info);
        assertTrue(result.isOk());
        assertEquals(info.id(), result.value().id());
    }

    @Test
    void updateUser_usernameChange() {
        var info = new UserUpdate(1L, Optional.of("changed"));
        var user = new UserEntity();
        user.setId(info.id());

        Mockito.when(userRepository.findById(info.id())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).then(returnsFirstArg());

        var result = userService.updateUser(info);
        assertTrue(result.isOk());
        assertEquals(info.id(), result.value().id());
        assertEquals(info.username().get(), result.value().username());
    }

    @Test
    void updateUser_idNotFound() {
        var info = new UserUpdate(2L, Optional.of("changed"));
        var result = userService.updateUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void updateUser_usernameTaken() {
        var info = new UserUpdate(1L, Optional.of("exists"));
        var user = new UserEntity();
        user.setId(info.id());

        Mockito.when(userRepository.findById(info.id())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsername(info.username().get())).thenReturn(true);

        var result = userService.updateUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.CONFLICT, result.error().status());
    }

    @Test
    void readUserById_ok() {
        var info = new UserId(1L);
        var user = new UserEntity();
        user.setId(info.id());

        Mockito.when(userRepository.findById(info.id())).thenReturn(Optional.of(user));

        var result = userService.readUserById(info);
        assertTrue(result.isOk());
        assertEquals(info.id(), result.value().id());
    }

    @Test
    void readUserById_idNotFound() {
        var info = new UserId(2L);
        var result = userService.readUserById(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readUserByUsername_ok() {
        var info = new UserUsername("user");
        var user = new UserEntity();
        user.setUsername("user");

        Mockito.when(userRepository.findByUsername(info.username())).thenReturn(Optional.of(user));

        var result = userService.readUserByUsername(info);
        assertTrue(result.isOk());
        assertEquals(info.username(), result.value().username());
    }

    @Test
    void readUserByUsername_usernameNotFound() {
        var info = new UserUsername("not found");
        var result = userService.readUserByUsername(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readUserVotesByUsername_ok() {
        var info = new UserUsername("user");
        var user = new UserEntity();
        user.setId(1L);

        Mockito.when(userRepository.findByUsername(info.username())).thenReturn(Optional.of(user));

        var result = userService.readUserVotesByUsername(info);
        assertTrue(result.isOk());
        assertEquals(user.getId(), result.value().user_id());
    }

    @Test
    void readUserVotesByUsername_usernameNotFound() {
        var info = new UserUsername("not found");
        var result = userService.readUserVotesByUsername(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readUserSurveysByUsername() {
        var info = new UserUsername("user");
        var user = new UserEntity();
        user.setUsername("user");

        Mockito.when(userRepository.findByUsername(info.username())).thenReturn(Optional.of(user));

        var result = userService.readUserSurveysByUsername(info);
        assertTrue(result.isOk());
    }

    @Test
    void readUserSurveysByUsername_userNameNotFound() {
        var info = new UserUsername("not found");
        var result = userService.readUserSurveysByUsername(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readAllUsers() {
        var result = userService.readAllUsers();
        assertTrue(result.isOk());
    }

    @Test
    void deleteUser_ok() {
        var info = new UserId(1L);

        Mockito.when(userRepository.existsById(info.id())).thenReturn(true);

        var result = userService.deleteUser(info);
        assertTrue(result.isOk());
        assertTrue(result.getOk().isEmpty());
    }

    @Test
    void deleteUser_idNotFound() {
        var info = new UserId(2L);
        var result = userService.deleteUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }
}