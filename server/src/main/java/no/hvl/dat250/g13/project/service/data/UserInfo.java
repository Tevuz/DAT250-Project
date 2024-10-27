package no.hvl.dat250.g13.project.service.data;

import no.hvl.dat250.g13.project.domain.UserEntity;

import java.util.Optional;

public record UserInfo(
        Optional<Long> id,
        Optional<String> username
) {
    public UserInfo(UserEntity user) {
        this(Optional.ofNullable(user.getId()), Optional.ofNullable(user.getUsername()));
    }

    public UserEntity into() {
        return new UserEntity(id.orElse(null), username.orElse(null));
    }
}
