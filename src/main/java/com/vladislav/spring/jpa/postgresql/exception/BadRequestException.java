package com.vladislav.spring.jpa.postgresql.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(final String msg) {
        super(msg);
    }
}
