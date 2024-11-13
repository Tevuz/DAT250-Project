package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;

import java.util.Objects;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UserKey id;

    private String username;

    // Default constructor (required by JPA)
    public UserEntity() {}

    // All-arguments constructor
    public UserEntity(UserKey id, String username) {
        this.id = id;
        this.username = username;
    }

    // Getters and Setters
    public UserKey getId() {
        return id;
    }

    public void setId(UserKey id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
