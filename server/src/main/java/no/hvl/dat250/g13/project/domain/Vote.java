package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long voterId;

    private Long surveyId;

    @OneToMany
    private List<OptionRef> options;

    // Default constructor (required by JPA)
    public Vote() {}

    // All-arguments constructor
    public Vote(Long id, Long voterId, Long surveyId, List<OptionRef> options) {
        this.id = id;
        this.voterId = voterId;
        this.surveyId = surveyId;
        this.options = options;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVoterId() {
        return voterId;
    }

    public void setVoterId(Long voter_id) {
        this.voterId = voter_id;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long survey_id) {
        this.surveyId = survey_id;
    }

    public List<OptionRef> getOptions() {
        return options;
    }

    public void setOptions(List<OptionRef> options) {
        this.options = options;
    }
}
