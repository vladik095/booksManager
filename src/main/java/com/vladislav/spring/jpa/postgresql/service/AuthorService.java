package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.model.Book;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import java.util.HashSet;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public AuthorService(AuthorRepository authorRepository, BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AuthorDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found"));
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
                .orElseThrow(() -> new IllegalArgumentException("Author with id " + id + " not found"));

        existingAuthor.setName(updatedAuthorDto.getName());
        authorRepository.save(existingAuthor);
    }

    private AuthorDto convertToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        authorDto.setBooks(author.getBooks().stream()
                .map(bookService::convertToDto)
                .collect(Collectors.toSet()));
        return authorDto;
    }

    private Author convertToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        return author;
    }

    public List<AuthorDto> createOrUpdateAuthorsBulk(List<AuthorDto> authorList) {
        return authorList.stream()
                .map(authorDto -> createOrUpdateAuthor(authorDto))
                .collect(Collectors.toList());
    }

    private AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        Author author;
        if (authorDto.getId() != null) {
            Optional<Author> existingAuthorOpt = authorRepository.findById(authorDto.getId());
            if (existingAuthorOpt.isPresent()) {
                author = existingAuthorOpt.get();
                // Обновляем данные существующего автора
                author.setName(authorDto.getName());
                // Очищаем список книг у автора перед обновлением
                author.getBooks().clear();
            } else {
                // Создаем нового автора
                author = new Author();
                author.setName(authorDto.getName());
            }
        } else {
            // Создаем нового автора
            author = new Author();
            author.setName(authorDto.getName());
        }

        // Проходим по списку книг и устанавливаем связь с автором
        for (BookDto bookDto : authorDto.getBooks()) {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());
            book.setAuthor(author); // Устанавливаем связь с автором
            author.getBooks().add(book);
        }

        authorRepository.save(author);
        return convertToDto(author);
    }
}
