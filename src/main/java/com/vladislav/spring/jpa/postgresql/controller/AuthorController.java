package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/get")
    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        logger.info("All authors fetched successfully.");
        return authors;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorById(id);
        if (author != null) {
            logger.info("Author with ID {} fetched successfully.", id);
            return ResponseEntity.ok(author);
        } else {
            logger.error("Author with ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<AuthorDto> saveAuthor(@Valid @RequestBody AuthorDto authorDto) {
        AuthorDto createdAuthor = authorService.addAuthor(authorDto);
        if (createdAuthor != null) {
            logger.info("Author saved successfully.");
            return ResponseEntity.ok(createdAuthor);
        } else {
            logger.error("Failed to save author.");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        logger.info("Author with ID {} deleted successfully.", id);
        return ResponseEntity.ok("Author deleted successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDto authorDto) {
        authorService.updateAuthor(id, authorDto);
        logger.info("Author with ID {} updated successfully.", id);
        return ResponseEntity.ok("Author updated successfully");
    }
}
