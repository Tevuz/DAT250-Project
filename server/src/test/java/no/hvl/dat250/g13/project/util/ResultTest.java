package no.hvl.dat250.g13.project.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    private static final Result<String, String> ok = new Result.Ok<>("value");
    private static final Result<String, String> error = new Result.Error<>("error");

    @Test
    void isOk() {
        assertTrue(ok.isOk());
        assertFalse(error.isOk());
    }

    @Test
    void isError() {
        assertFalse(ok.isError());
        assertTrue(error.isError());
    }

    @Test
    void getOk() {
        assertEquals(Optional.of("value"), ok.getOk());
        assertEquals(Optional.empty(), error.getOk());
    }

    @Test
    void getError() {
        assertEquals(Optional.empty(), ok.getError());
        assertEquals(Optional.of("error"), error.getError());
    }

    @Test
    void value() {
        assertEquals("value", ok.value());
        assertThrows(UnsupportedOperationException.class, error::value);
    }

    @Test
    void error() {
        assertThrows(UnsupportedOperationException.class, ok::error);
        assertEquals("error", error.error());
    }
}