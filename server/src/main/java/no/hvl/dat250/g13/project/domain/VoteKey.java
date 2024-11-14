package no.hvl.dat250.g13.project.domain;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record VoteKey(
        @NotNull Long userId,
        @NotNull Long surveyId,
        @NotNull Long optionId
) implements Serializable {}
