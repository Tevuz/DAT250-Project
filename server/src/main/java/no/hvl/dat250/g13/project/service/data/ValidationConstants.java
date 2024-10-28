package no.hvl.dat250.g13.project.service.data;

public class ValidationConstants {

    private ValidationConstants() {}

    public static final int USERNAME_LENGTH_MIN = 3;
    public static final int USERNAME_LENGTH_MAX = 20;
    public static final String USERNAME_PATTERN = "^[a-zA-Z0-9_-]*$";
}
