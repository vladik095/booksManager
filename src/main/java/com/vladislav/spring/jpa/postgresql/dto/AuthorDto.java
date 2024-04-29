package com.vladislav.spring.jpa.postgresql.dto;

import java.util.Set;

public class AuthorDto {

    private Long id;
    private String name;
    private String description; // Новое поле описания
    private Set<BookDto> books;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name, String description, Set<BookDto> books) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.books = books;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BookDto> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDto> books) {
        this.books = books;
    }
}
