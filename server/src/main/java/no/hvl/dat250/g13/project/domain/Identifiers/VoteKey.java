package no.hvl.dat250.g13.project.domain.Identifiers;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record VoteKey(
        @NotNull UserKey userId,
        @NotNull SurveyKey surveyId,
        @NotNull OptionKey optionId
) implements Serializable { }
