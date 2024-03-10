package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.model.Book;
import com.vladislav.spring.jpa.postgresql.service.BookService;
import com.vladislav.spring.jpa.postgresql.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping("/{authorId}")
    public Book addBook(@PathVariable Long authorId, @RequestBody Book book) {
        return bookService.addBook(authorId, book);
    }

    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody Book book) {
        bookService.updateBook(id, book);
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
    public Set<Tag> getTagsByBookId(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        return book.getTags();
    }

    @GetMapping("/tags/{tagId}")
    public Set<Book> getBooksByTagId(@PathVariable Long tagId) {
        return bookService.getBooksByTagId(tagId);
    }
}
