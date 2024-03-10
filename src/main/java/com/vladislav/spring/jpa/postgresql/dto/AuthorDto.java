package com.vladislav.spring.jpa.postgresql.dto;

import java.util.HashSet;
import java.util.Set;

public class AuthorDto {
    private Long id;
    private String name;
    private Set<BookDto> books = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BookDto> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDto> books) {
        this.books = books;
    }
}
