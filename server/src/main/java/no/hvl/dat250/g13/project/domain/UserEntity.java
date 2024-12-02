package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String googleId;
    private String githubId;

    // Default constructor (required by JPA)
    public UserEntity() {}

    // All-arguments constructor
    public UserEntity(Long id, String username, String password) {
        setId(id);
        setUsername(username);
        setPassword(password);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String google_id) {
        this.googleId = google_id;
    }

    public String getGithubId() {
        return githubId;
    }

    public void setGithubId(String github_id) {
        this.githubId = github_id;
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
