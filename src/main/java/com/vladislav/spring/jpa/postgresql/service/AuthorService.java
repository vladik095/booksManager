package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.cache.BookCache;
import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.model.Book;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import java.util.Collections;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;
    private final RequestCounterService requestCounterService;
    private final TagService tagService;
    private final BookCache bookCache;

    private static final String NOT_FOUND_MESSAGE = " not found";

    public AuthorService(AuthorRepository authorRepository, BookService bookService,
            RequestCounterService requestCounterService, TagService tagService, BookCache bookCache) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
        this.requestCounterService = requestCounterService;
        this.tagService = tagService;
        this.bookCache = bookCache;
    }

    public List<AuthorDto> getAllAuthors() {
        requestCounterService.incrementCounter();
        return authorRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + NOT_FOUND_MESSAGE));
        return convertToDto(author);
    }

    public AuthorDto addAuthor(AuthorDto authorDto) {
        Author author = convertToEntity(authorDto);
        return convertToDto(authorRepository.save(author));
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    public void updateAuthor(Long id, AuthorDto updatedAuthorDto) {
        Author existingAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + NOT_FOUND_MESSAGE));

        existingAuthor.setName(updatedAuthorDto.getName());
        authorRepository.save(existingAuthor);
    }

    public AuthorDto getAuthorByName(String name) {
        Author author = authorRepository.findByName(name);
        if (author != null) {
            return convertToDto(author);
        } else {
            throw new IllegalArgumentException("Author with name " + name + NOT_FOUND_MESSAGE);
        }
    }

    private AuthorDto convertToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setDescription(author.getDescription());
        authorDto.setBooks(author.getBooks().stream()
                .map(book -> {
                    BookDto bookDto = bookService.convertToDto(book);
                    // Загрузка тегов для каждой книги
                    bookDto.setTags(book.getTags().stream()
                            .map(tagService::convertToDto)
                            .collect(Collectors.toSet()));
                    // Устанавливаем теги без поля books

                    return bookDto;
                })
                .collect(Collectors.toSet()));
        return authorDto;
    }

    private Author convertToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        author.setDescription(authorDto.getDescription()); // Добавлено присвоение описания автора
        return author;
    }

    public List<AuthorDto> createOrUpdateAuthorsBulk(List<AuthorDto> authorList) {
        return authorList.stream()
                .map(authorDto -> createOrUpdateAuthor(authorDto))
                .toList();
    }

    private AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        Author author;
        if (authorDto.getId() != null) {
            Optional<Author> existingAuthorOpt = authorRepository.findById(authorDto.getId());
            if (existingAuthorOpt.isPresent()) {
                author = existingAuthorOpt.get();

                author.setName(authorDto.getName());

                author.getBooks().clear();
            } else {

                author = new Author();
                author.setName(authorDto.getName());
            }
        } else {

            author = new Author();
            author.setName(authorDto.getName());
        }

        for (BookDto bookDto : authorDto.getBooks()) {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(author);
            author.getBooks().add(book);
        }

        authorRepository.save(author);
        return convertToDto(author);
    }

    public List<AuthorDto> findAuthorsByNameContaining(String keyword) {

        List<Author> authors = authorRepository.findByNameContaining(keyword);
        if (authors != null) {
            List<AuthorDto> authorDtos = authors.stream()
                    .map(this::convertToDto)
                    .toList();

            return authorDtos;
        } else {
            return Collections.emptyList();
        }
    }

}
