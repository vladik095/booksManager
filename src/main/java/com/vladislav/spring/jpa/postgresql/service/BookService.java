package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.model.Book;
import com.vladislav.spring.jpa.postgresql.model.Tag;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import com.vladislav.spring.jpa.postgresql.repository.BookRepository;
import com.vladislav.spring.jpa.postgresql.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BookService {

    private static final String NOT_FOUND_MESSAGE = " not found";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + NOT_FOUND_MESSAGE));
    }

    public Book addBook(Long authorId, Book book) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Author with id " + authorId + NOT_FOUND_MESSAGE));
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void updateBook(Long id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    public void addTagToBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book" + NOT_FOUND_MESSAGE));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag" + NOT_FOUND_MESSAGE));
        book.getTags().add(tag);
        bookRepository.save(book);
    }

    public Set<Book> getBooksByTagId(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag with id " + tagId + NOT_FOUND_MESSAGE));
        return tag.getBooks();
    }
}
