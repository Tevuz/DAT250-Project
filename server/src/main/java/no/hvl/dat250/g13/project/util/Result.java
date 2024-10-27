package no.hvl.dat250.g13.project.util;

import java.util.Optional;

public abstract sealed class Result<T, E> permits Result.Ok, Result.Error{

    public Optional<T> getOk() {
        return Optional.empty();
    }

    public Optional<E> getError() {
        return Optional.empty();
    }

    public boolean isOk() {
        return false;
    }

    public boolean isError() {
        return false;
    }

    public static final class Ok<T, E> extends Result<T, E> {
        private T value;

        public <S extends T> Ok(S value) {
            this.value = value;
        }

        @Override
        public boolean isOk() {
            return true;
        }

        public Optional<T> getOk() {
            return Optional.of(value);
        }
    }

    public static final class Error<T, E> extends Result<T, E> {
        public E error;

        public <S extends E>  Error(S error) {
            this.error = error;
        }

        public Optional<E> getError() {
            return Optional.of(error);
        }

        @Override
        public boolean isError() {
            return true;
        }
    }
}
