package com.vladislav.spring.jpa.postgresql.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIdentityReference(alwaysAsId = true)
public class AuthorDto implements DtoWithBooks {
    private Long id;
    private String name;
    private Set<BookDto> books = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<BookDto> getBooks() {
        return books;
    }

    @Override
    public void setBooks(Set<BookDto> books) {
        this.books = books;
    }
}
