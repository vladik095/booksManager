package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.vladislav.spring.jpa.postgresql.dto.TagDto;

import java.util.Set;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/{authorId}")
    public BookDto addBook(@PathVariable Long authorId, @RequestBody BookDto bookDto) {
        return bookService.addBook(authorId, bookDto);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody BookDto bookDto) {
        bookService.updateBook(id, bookDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @PostMapping("/{bookId}/tags/{tagId}")
    public void addTagToBook(@PathVariable Long bookId, @PathVariable Long tagId) {
        bookService.addTagToBook(bookId, tagId);
    }

    @GetMapping("/{bookId}/tags")
    public Set<TagDto> getTagsByBookId(@PathVariable Long bookId) {
        return bookService.getTagsByBookId(bookId);
    }

    @GetMapping("/tags/{tagId}")
    public Set<BookDto> getBooksByTagId(@PathVariable Long tagId) {
        return bookService.getBooksByTagId(tagId);
    }
}
