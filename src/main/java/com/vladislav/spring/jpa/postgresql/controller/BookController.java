package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private static final String SUCCESS_MESSAGE = "Success";
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = bookService.getAllBooks();
        logger.info("All books fetched successfully.");
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        if (book != null) {
            logger.info("Book with ID {} fetched successfully.", id);
            return ResponseEntity.ok(book);
        } else {
            logger.error("Book with ID {} not found.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{authorId}")
    public ResponseEntity<String> addBook(@PathVariable Long authorId, @RequestBody BookDto bookDto) {
        BookDto newBook = bookService.addBook(authorId, bookDto);
        if (newBook != null) {
            logger.info("Book saved successfully.");
            return new ResponseEntity<>(SUCCESS_MESSAGE, HttpStatus.CREATED);
        } else {
            logger.error("Failed to save book.");
            return new ResponseEntity<>("Book with this title already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        bookService.updateBook(id, bookDto);
        logger.info("Book with ID {} updated successfully.", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        logger.info("Book with ID {} deleted successfully.", id);
        return ResponseEntity.ok(SUCCESS_MESSAGE);
    }

    @PostMapping("/{bookId}/tags/{tagId}")
    public ResponseEntity<Void> addTagToBook(@PathVariable Long bookId, @PathVariable Long tagId) {
        bookService.addTagToBook(bookId, tagId);
        logger.info("Tag added to book with ID {} successfully.", bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookId}/tags")
    public ResponseEntity<Set<TagDto>> getTagsByBookId(@PathVariable Long bookId) {
        Set<TagDto> tags = bookService.getTagsByBookId(bookId);
        logger.info("Tags fetched for book with ID {} successfully.", bookId);
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/tags/{tagId}")
    public ResponseEntity<Set<BookDto>> getBooksByTagId(@PathVariable Long tagId) {
        Set<BookDto> books = bookService.getBooksByTagId(tagId);
        logger.info("Books fetched for tag with ID {} successfully.", tagId);
        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/{bookId}/tags/{tagId}")
    public ResponseEntity<String> deleteTagFromBook(@PathVariable Long bookId, @PathVariable Long tagId) {
        bookService.deleteTagFromBook(bookId, tagId);
        logger.info("Tag with ID {} deleted from book with ID {} successfully.", tagId, bookId);
        return ResponseEntity.ok(SUCCESS_MESSAGE);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDto>> getBooksByTitleContaining(@RequestParam("keyword") String keyword) {
        List<BookDto> books = bookService.findBooksByTitleContaining(keyword);
        String sanitizedKeyword = keyword;
        if (shouldSanitize(keyword)) {
            sanitizedKeyword = sanitize(keyword);
        }
        logger.info("Books containing sanitized keyword fetched successfully.");
        return ResponseEntity.ok(books);
    }

    private boolean shouldSanitize(String input) {
        return input.matches(".*+[^a-zA-Z0-9].*+");
    }

    private String sanitize(String input) {
        return input.replaceAll("[^a-zA-Z0-9]", "_");
    }

}
