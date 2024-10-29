package no.hvl.dat250.g13.project.domain.Identifiers;

import java.io.Serializable;

public record VoteKey(UserKey userId, SurveyKey surveyId, OptionKey optionId) implements Serializable {
}
