package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.dto.TagDto;
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
import java.util.stream.Collectors;

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

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + id + NOT_FOUND_MESSAGE));
        return convertToDto(book);
    }

    public BookDto addBook(Long authorId, BookDto bookDto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Author with id " + authorId + NOT_FOUND_MESSAGE));

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);
        author.getBooks().add(book);

        return convertToDto(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void updateBook(Long id, BookDto bookDto) {
        Book book = convertToEntity(bookDto);
        book.setId(id);
        bookRepository.save(book);
    }

    public void addTagToBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book" + NOT_FOUND_MESSAGE));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag" + NOT_FOUND_MESSAGE));
        book.getTags().add(tag);
        bookRepository.save(book);
    }

    public Set<TagDto> getTagsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book with id " + bookId + NOT_FOUND_MESSAGE));
        return book.getTags().stream()
                .map(this::convertToTagDto)
                .collect(Collectors.toSet());
    }

    public Set<BookDto> getBooksByTagId(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag with id " + tagId + NOT_FOUND_MESSAGE));
        return tag.getBooks().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    private Book convertToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());

        return book;
    }

    private BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());

        return bookDto;
    }

    private TagDto convertToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }
}
