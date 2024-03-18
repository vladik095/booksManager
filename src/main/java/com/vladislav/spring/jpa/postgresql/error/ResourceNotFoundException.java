package com.vladislav.spring.jpa.postgresql.error;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, String field, Long id) {
        super(String.format("%s not found with %s : '%s'", resource, field, id));
    }
}
