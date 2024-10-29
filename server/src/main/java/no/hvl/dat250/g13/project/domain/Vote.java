package no.hvl.dat250.g13.project.domain;

import jakarta.persistence.*;
import no.hvl.dat250.g13.project.domain.Identifiers.OptionKey;
import no.hvl.dat250.g13.project.domain.Identifiers.SurveyKey;
import no.hvl.dat250.g13.project.domain.Identifiers.UserKey;
import no.hvl.dat250.g13.project.domain.Identifiers.VoteKey;

import java.util.Objects;

@Entity
@IdClass(VoteKey.class)
public class Vote {

    @Id
    private UserKey userId;
    @Id
    private SurveyKey surveyId;
    @Id
    private OptionKey optionId;

    // Default constructor (required by JPA)
    public Vote() {
    }

    // All-arguments constructor
    public Vote(UserKey userId, SurveyKey surveyId, OptionKey optionId) {
        this.userId = userId;
        this.surveyId = surveyId;
        this.optionId = optionId;
    }


    // Getters and Setters

    public UserKey getUserId() {
        return userId;
    }

    public void setUserId(UserKey userId) {
        this.userId = userId;
    }

    public SurveyKey getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(SurveyKey surveyId) {
        this.surveyId = surveyId;
    }

    public OptionKey getOptionId() {
        return optionId;
    }

    public void setOptionId(OptionKey optionId) {
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
