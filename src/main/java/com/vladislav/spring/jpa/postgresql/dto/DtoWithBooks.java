package com.vladislav.spring.jpa.postgresql.dto;

import java.util.Set;

public interface DtoWithBooks {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Set<BookDto> getBooks();

    void setBooks(Set<BookDto> books);
}
