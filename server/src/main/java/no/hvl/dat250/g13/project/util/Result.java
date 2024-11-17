package no.hvl.dat250.g13.project.util;

import java.util.Optional;
import java.util.stream.Stream;

public abstract sealed class Result<T, E> permits Result.Ok, Result.Error{

    public Optional<T> getOk() {
        return Optional.empty();
    }

    public T value() {
        throw new UnsupportedOperationException("Result is not ok");
    }

    public Optional<E> getError() {
        return Optional.empty();
    }

    public E error() {
        throw new UnsupportedOperationException("Result is not error");
    }

    public boolean isOk() {
        return false;
    }

    public boolean isError() {
        return false;
    }

    public Stream<T> stream() {
        return Stream.empty();
    }

    public static final class Ok<T, E> extends Result<T, E> {
        private final T value;

        public <S extends T> Ok(S value) {
            this.value = value;
        }

        @Override
        public boolean isOk() {
            return true;
        }

        public Optional<T> getOk() {
            return Optional.ofNullable(value);
        }

        public T value() {
            return value;
        }

        public Stream<T> stream() {
            return Stream.of(value);
        }
    }

    public static final class Error<T, E> extends Result<T, E> {
        public final E error;

        public <S extends E>  Error(S error) {
            this.error = error;
        }

        @Override
        public boolean isError() {
            return true;
        }

        public Optional<E> getError() {
            return Optional.ofNullable(error);
        }

        public E error() {
            return error;
        }

    }
}
