package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poll> polls = new ArrayList<>();

    private Long authorId;

    @Transient
    private Long voteTotal;

    // Default constructor (required by JPA)
    public Survey() {}

    // All-arguments constructor
    public Survey(Long id, String title, List<Poll> polls, Long authorId) {
        setId(id);
        setTitle(title);
        setPolls(polls);
        setAuthorId(authorId);
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
        if (this.polls != null)
            this.polls.forEach(poll -> poll.setSurvey(null));
        if (polls != null)
            polls.forEach(poll -> poll.setSurvey(this));
        this.polls.clear();
        this.polls.addAll(polls);
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long author) {
        this.authorId = author;
    }

    public Long getVoteTotal() {
        return voteTotal;
    }

    public void setVoteTotal(Long voteTotal) {
        this.voteTotal = voteTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id) && Objects.equals(title, survey.title) && Objects.equals(polls, survey.polls) && Objects.equals(authorId, survey.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, polls, authorId);
    }
}