package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;

import java.util.List;
import java.util.Objects;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private SurveyKey id;

    private String title;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Poll> polls;

    private UserKey author;

    // Default constructor (required by JPA)
    public Survey() {}

    // All-arguments constructor
    public Survey(SurveyKey id, String title, List<Poll> polls, UserKey author) {
        setId(id);
        setTitle(title);
        setPolls(polls);
        setAuthor(author);
    }

    // Getters and Setters
    public SurveyKey getId() {
        return id;
    }

    public void setId(SurveyKey id) {
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
        this.polls = polls;
    }

    public UserKey getAuthor() {
        return author;
    }

    public void setAuthor(UserKey author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Survey survey = (Survey) o;
        return Objects.equals(id, survey.id) && Objects.equals(title, survey.title) && Objects.equals(polls, survey.polls) && Objects.equals(author, survey.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, polls, author);
    }
}