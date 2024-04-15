package com.vladislav.spring.jpa.postgresql.dto;

import java.util.Set;

public class BookDto {

    private Long id;

    private String title;
    private AuthorDto author;
    private Set<TagDto> tags;

    public BookDto() {
    }

    public BookDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;

    }

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }
}
