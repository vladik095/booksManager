package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.model.Book;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import com.vladislav.spring.jpa.postgresql.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
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

    private TagDto convertTagToDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    private BookDto convertBookToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());

        Set<TagDto> tagDtos = book.getTags().stream()
                .map(this::convertTagToDto) 
                .collect(Collectors.toSet());
        bookDto.setTags(tagDtos);

        return bookDto;
    }

    private AuthorDto convertToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());

        Set<BookDto> bookDtos = author.getBooks().stream()
                .map(this::convertBookToDto) 
                .collect(Collectors.toSet());
        authorDto.setBooks(bookDtos);

        return authorDto;
    }

    private Author convertToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        return author;
    }
}
