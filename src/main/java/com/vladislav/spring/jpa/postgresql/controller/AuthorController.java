package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Set;
import com.vladislav.spring.jpa.postgresql.model.Book;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @GetMapping("/{id}")
    public Author getAuthorById(@PathVariable Long id) {
        return authorService.getAuthorById(id);
    }

    @PostMapping
    public void addAuthor(@RequestBody Author author) {
        authorService.addAuthor(author);
    }

    @GetMapping("/{id}/books")
    public Set<Book> getBooksByAuthorId(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        return author.getBooks();
    }

    @PutMapping("/{id}")
    public void updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        authorService.updateAuthor(id, author);
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }
}
