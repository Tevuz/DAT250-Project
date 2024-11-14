package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@IdClass(VoteKey.class)
public class Vote {

    @Id
    private Long userId;
    @Id
    private Long surveyId;
    @Id
    private Long optionId;

    // Default constructor (required by JPA)
    public Vote() {
    }

    // All-arguments constructor
    public Vote(Long userId, Long surveyId, Long optionId) {
        setUserId(userId);
        setSurveyId(surveyId);
        setOptionId(optionId);
    }


    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(userId, vote.userId) && Objects.equals(surveyId, vote.surveyId) && Objects.equals(optionId, vote.optionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, surveyId, optionId);
    }
}
