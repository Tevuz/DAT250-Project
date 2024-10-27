package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.UserEntity;

public record UserInfo(
        Long id,
        String username
) {
    public UserInfo(UserEntity user) {
        this(user.getId(), user.getUsername());
    }

    public UserEntity into() {
        return new UserEntity(id, username);
    }
}
