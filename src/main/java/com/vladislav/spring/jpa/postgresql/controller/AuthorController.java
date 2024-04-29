package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.exception.BadRequestException;
import com.vladislav.spring.jpa.postgresql.service.AuthorService;

import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/getAll")
    public List<AuthorDto> getAllAuthors() {
        List<AuthorDto> authors = authorService.getAllAuthors();
        logger.info("All authors fetched successfully.");
        return authors;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<AuthorDto> fetchAuthorById(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorById(id);
        if (author != null) {
            logger.info("Author with ID {} fetched successfully.", id);
            return ResponseEntity.ok(author);
        } else {
            logger.error("Author with ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<AuthorDto> fetchAuthorByName(@PathVariable String name) {
        AuthorDto author = authorService.getAuthorByName(name);
        if (author != null) {
            logger.info("Author with name {} fetched successfully.", name);
            return ResponseEntity.ok(author);
        } else {
            logger.error("Author with name {} not found.", name);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<AuthorDto> saveAuthor(@RequestBody(required = false) AuthorDto author) {
        if (author == null || author.getName() == null) {
            throw new BadRequestException("Author name is required");
        }

        AuthorDto createdAuthor = authorService.addAuthor(author);
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
    public ResponseEntity<String> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorDto author) {
        authorService.updateAuthor(id, author);
        logger.info("Author with ID {} updated successfully.", id);
        return ResponseEntity.ok("Author updated successfully");
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<AuthorDto>> createOrUpdateAuthorsBulk(@RequestBody List<AuthorDto> authorList) {
        logger.info("Creating or updating authors in bulk");

        List<AuthorDto> createdOrUpdatedAuthors = authorService.createOrUpdateAuthorsBulk(authorList);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrUpdatedAuthors);
    }
}
