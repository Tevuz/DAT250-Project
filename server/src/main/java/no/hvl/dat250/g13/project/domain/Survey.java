package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Poll> polls;

    @ManyToOne
    private User author;

    // Default constructor (required by JPA)
    public Survey() {}

    // All-arguments constructor
    public Survey(Long id, String title, List<Poll> polls, User author) {
        this.id = id;
        this.title = title;
        this.polls = polls;
        this.author = author;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Poll> getPolls() {
        return polls;
    }

    public void setPolls(List<Poll> polls) {
        this.polls = polls;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}