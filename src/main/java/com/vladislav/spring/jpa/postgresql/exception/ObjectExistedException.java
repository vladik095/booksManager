package com.vladislav.spring.jpa.postgresql.exception;

public class ObjectExistedException extends RuntimeException {

    public ObjectExistedException(final String mes) {
        super(mes);
    }
}