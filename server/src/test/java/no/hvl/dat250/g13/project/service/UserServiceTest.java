package no.hvl.dat250.g13.project.service;

import no.hvl.dat250.g13.project.domain.UserEntity;
import no.hvl.dat250.g13.project.repository.SurveyRepository;
import no.hvl.dat250.g13.project.repository.UserRepository;
import no.hvl.dat250.g13.project.repository.VoteRepository;
import no.hvl.dat250.g13.project.service.data.user.UserCreate;
import no.hvl.dat250.g13.project.service.data.user.UserId;
import no.hvl.dat250.g13.project.service.data.user.UserUpdate;
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
        Mockito.when(userRepository.existsByUsername(info.username().orElse(null))).thenReturn(true);

        var result = userService.updateUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.CONFLICT, result.error().status());
    }

    @Test
    void readUser_ok() {
        var info = new UserId(1L);
        var user = new UserEntity();
        user.setId(info.id().orElse(null));

        Mockito.when(userRepository.findBy(info)).thenReturn(Optional.of(user));

        var result = userService.readUser(info);
        assertTrue(result.isOk());
        assertEquals(info.id(), Optional.of(result.value().id()));
    }

    @Test
    void readUserNotFound() {
        var info = new UserId(2L);
        var result = userService.readUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readUser_username() {
        var info = new UserId("user");
        var user = new UserEntity();
        user.setUsername("user");

        Mockito.when(userRepository.findBy(info)).thenReturn(Optional.of(user));

        var result = userService.readUser(info);
        assertTrue(result.isOk());
        assertEquals(info.username(), Optional.of(result.value().username()));
    }

    @Test
    void readUser_usernameNotFound() {
        var info = new UserId("not found");
        var result = userService.readUser(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readUserVotes_ok() {
        var info = new UserId("user");
        var user = new UserEntity();
        user.setId(1L);

        Mockito.when(userRepository.findBy(info)).thenReturn(Optional.of(user));

        var result = userService.readUserVotes(info);
        assertTrue(result.isOk());
        assertEquals(user.getId(), result.value().user_id());
    }

    @Test
    void readUserVotes_notFound() {
        var info = new UserId("not found");
        var result = userService.readUserVotes(info);
        assertTrue(result.isError());
        assertEquals(HttpStatus.NOT_FOUND, result.error().status());
    }

    @Test
    void readUserSurveys() {
        var info = new UserId("user");
        var user = new UserEntity();
        user.setUsername("user");

        Mockito.when(userRepository.findBy(info)).thenReturn(Optional.of(user));

        var result = userService.readUserSurveys(info);
        assertTrue(result.isOk());
    }

    @Test
    void readUserSurveys_notFound() {
        var info = new UserId("not found");
        var result = userService.readUserSurveys(info);
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

        Mockito.when(userRepository.existsBy(info)).thenReturn(true);

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