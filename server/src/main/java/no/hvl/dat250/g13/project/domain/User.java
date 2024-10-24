package no.hvl.dat250.g13.project.domain;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToMany(mappedBy = "author")
    private List<Survey> surveys;

    @OneToMany(mappedBy = "voter")
    private List<Vote> votes;

    // Default constructor (required by JPA)
    public User() {}

    // All-arguments constructor
    public User(Long id, String username, List<Survey> surveys, List<Vote> votes) {
        this.id = id;
        this.username = username;
        this.surveys = surveys;
        this.votes = votes;
    }


}
