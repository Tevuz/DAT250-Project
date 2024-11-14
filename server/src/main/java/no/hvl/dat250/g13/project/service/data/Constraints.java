package no.hvl.dat250.g13.project.service.data;

public class Constraints {

    // ---- Option Messages ----
    public static final String MESSAGE_OPTION_ID_REQUIRED = "Option value is required";
    public static final String MESSAGE_OPTION_TEXT_REQUIRED = "Option text is required";

    // ---- Poll Messages ----
    public static final String MESSAGE_POLL_ID_REQUIRED = "Poll value is required";
    public static final String MESSAGE_POLL_TEXT_REQUIRED = "Poll text is required";
    public static final String MESSAGE_POLL_OPTIONS_REQUIRED = "Poll options is required";

    // ---- SurveyDTO Messages ----
    public static final String MESSAGE_SURVEY_ID_REQUIRED = "Survey value is required";
    public static final String MESSAGE_SURVEY_AUTHOR_ID_REQUIRED = "Survey author value is required";
    public static final String MESSAGE_SURVEY_TITLE_REQUIRED = "Survey title is required";
    public static final String MESSAGE_SURVEY_POLLS_REQUIRED = "Survey polls is required";

    // ---- User Constraints ----
    public static final int USERNAME_LENGTH_MIN = 3;
    public static final int USERNAME_LENGTH_MAX = 20;
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]*$";

    // ---- User Messages ----
    public static final String MESSAGE_USER_ID_REQUIRED = "User value is required";
    public static final String MESSAGE_USERNAME_REQUIRED = "Username is required";
    public static final String MESSAGE_USERNAME_LENGTH = "Username must be between 3 and 20 characters long";
    public static final String MESSAGE_USERNAME_PATTERN = "Username can only contain: A-Z a-z 0-9 _ -";

    // ---- Vote Messages ----
    public static final String MESSAGE_VOTE_SURVEY_ID_REQUIRED = "Survey value is required";
    public static final String MESSAGE_VOTE_USER_ID_REQUIRED = "User value is required";
    public static final String MESSAGE_VOTE_OPTIONS_REQUIRED = "Selected options is required";

}
