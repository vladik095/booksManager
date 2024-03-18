package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

<<<<<<< HEAD
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

=======
>>>>>>> 171ceee (Add bookCache)
    private AuthorDto convertToDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
<<<<<<< HEAD

        Set<BookDto> bookDtos = author.getBooks().stream()
                .map(this::convertBookToDto) 
                .collect(Collectors.toSet());
        authorDto.setBooks(bookDtos);

=======
        authorDto.setBooks(author.getBooks().stream()
                .map(bookService::convertToDto)
                .collect(Collectors.toSet()));
>>>>>>> 171ceee (Add bookCache)
        return authorDto;
    }

    private Author convertToEntity(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setName(authorDto.getName());
        return author;
    }
}
