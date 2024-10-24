package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.User;

import java.util.List;

public record UserInfo(
        Long id,
        String username
) {
    public UserInfo(User user) {
        this(user.getId(), user.getUsername());
    }

    public User into() {
        return new User(id, username);
    }
}
