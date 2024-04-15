package com.vladislav.spring.jpa;

import com.vladislav.spring.jpa.postgresql.service.AuthorService;
import com.vladislav.spring.jpa.postgresql.service.BookService;

import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAuthors() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Author 1");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Author 2");

        when(authorRepository.findAll()).thenReturn(Arrays.asList(author1, author2));

        List<AuthorDto> authors = authorService.getAllAuthors();

        assertEquals(2, authors.size());
        assertEquals("Author 1", authors.get(0).getName());
        assertEquals("Author 2", authors.get(1).getName());
    }

    @Test
    void getAuthorById() {

        Author author = new Author();
        author.setId(1L);
        author.setName("Author");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDto authorDto = authorService.getAuthorById(1L);

        assertEquals("Author", authorDto.getName());
    }

    @Test
    void addAuthor() {

        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("New Author");

        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setName("New Author");

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorDto savedAuthorDto = authorService.addAuthor(authorDto);

        assertEquals(1L, savedAuthorDto.getId());
        assertEquals("New Author", savedAuthorDto.getName());
    }

    @Test
    void deleteAuthor() {

        Author author = new Author();
        author.setId(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateAuthor() {

        Author existingAuthor = new Author();
        existingAuthor.setId(1L);
        existingAuthor.setName("Old Name");

        AuthorDto updatedAuthorDto = new AuthorDto();
        updatedAuthorDto.setName("New Name");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));

        authorService.updateAuthor(1L, updatedAuthorDto);

        assertEquals("New Name", existingAuthor.getName());
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void createOrUpdateAuthorsBulk() {

        AuthorDto authorDto1 = new AuthorDto(null, "Author 1", Collections.emptySet());
        AuthorDto authorDto2 = new AuthorDto(null, "Author 2", Collections.emptySet());

        List<AuthorDto> authorDtoList = Arrays.asList(authorDto1, authorDto2);

        authorService.createOrUpdateAuthorsBulk(authorDtoList);

        verify(authorRepository, times(2)).save(any(Author.class));
    }

    @Test
    void getAuthorById_NotFound() {

        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authorService.getAuthorById(100L));
    }

    @Test
    void deleteAuthor_NotFound() {

        doThrow(EmptyResultDataAccessException.class).when(authorRepository).deleteById(anyLong());

        assertThrows(EmptyResultDataAccessException.class, () -> authorService.deleteAuthor(100L));
    }

}
