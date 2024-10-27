package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
public class Vote {

    @Embeddable
    public record VoteKey(Long voterId, Long surveyId) implements Serializable {}

    @EmbeddedId
    private VoteKey id;

    @OneToMany
    private List<OptionRef> options;

    // Default constructor (required by JPA)
    public Vote() {}

    // All-arguments constructor
    public Vote(Long voterId, Long surveyId, List<OptionRef> options) {
        this.id = new VoteKey(voterId, surveyId);
        this.options = options;
    }

    // Getters and Setters
    public VoteKey getId() {
        return id;
    }

    public void setId(VoteKey id) {
        this.id = id;
    }

    public void setId(Long voterId, Long surveyId) {
        this.id = new VoteKey(voterId, surveyId);
    }

    public Long getVoterId() {
        return id.voterId;
    }

    public void setVoterId(Long voterId) {
        this.id = new VoteKey(voterId, id.surveyId);
    }

    public Long getSurveyId() {
        return id.surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.id = new VoteKey(id.voterId, surveyId);
    }

    public List<OptionRef> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRef> options) {
        this.options = options;
    }
}
