package com.vladislav.spring.jpa.postgresql.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(final String msg) {
        super(msg);
    }
}