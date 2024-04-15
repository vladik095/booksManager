package com.vladislav.spring.jpa;

import com.vladislav.spring.jpa.postgresql.cache.BookCache;
import com.vladislav.spring.jpa.postgresql.dto.BookDto;
import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.error.ResourceNotFoundException;
import com.vladislav.spring.jpa.postgresql.model.Author;
import com.vladislav.spring.jpa.postgresql.model.Book;
import com.vladislav.spring.jpa.postgresql.model.Tag;
import com.vladislav.spring.jpa.postgresql.repository.AuthorRepository;
import com.vladislav.spring.jpa.postgresql.repository.BookRepository;
import com.vladislav.spring.jpa.postgresql.repository.TagRepository;
import com.vladislav.spring.jpa.postgresql.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookCache bookCache;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deleteBook() {
        Book book = new Book();
        book.setId(1L);
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void addTagToBook() {
        Book book = new Book();
        book.setId(1L);
        Tag tag = new Tag();
        tag.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        bookService.addTagToBook(1L, 1L);
        assertEquals(1, book.getTags().size());
        assertTrue(book.getTags().contains(tag));
    }

    @Test
    void deleteTagFromBook() {
        Book book = new Book();
        book.setId(1L);
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tags.add(tag);
        book.setTags(tags);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        bookService.deleteTagFromBook(1L, 1L);
        assertFalse(book.getTags().contains(tag));
    }

    @Test
    void getBooksByTagId() {
        Tag tag = new Tag();
        tag.setId(1L);
        Book book = new Book();
        book.setId(1L);
        tag.getBooks().add(book);
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        Set<BookDto> result = bookService.getBooksByTagId(1L);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(b -> b.getId() == 1L));
    }

    @Test
    void getTagsByBookId() {
        Book book = new Book();
        book.setId(1L);
        Set<Tag> tags = new HashSet<>();
        Tag tag = new Tag();
        tag.setId(1L);
        tags.add(tag);
        book.setTags(tags);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Set<TagDto> result = bookService.getTagsByBookId(1L);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(t -> t.getId() == 1L));
    }

    @Test
    void updateBook() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Updated Title");
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Original Title");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        bookService.updateBook(1L, bookDto);
        assertEquals("Updated Title", existingBook.getTitle());
    }

    @Test
    void getBookById() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        BookDto result = bookService.getBookById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getAllBooks() {
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);
        List<BookDto> result = bookService.getAllBooks();
        assertEquals(2, result.size());
    }

    @Test
    void convertToDtoTest() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        BookDto result = bookService.convertToDto(book);
        assertEquals(book.getId(), result.getId());
        assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    void convertToEntityFromDtoTest() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Test Book");
        Book result = bookService.convertToEntityFromDto(bookDto);
        assertEquals(bookDto.getId(), result.getId());
        assertEquals(bookDto.getTitle(), result.getTitle());
    }

    @Test
    void addBookWithTags() {
        Author author = new Author();
        author.setId(1L);
        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book Title");
        TagDto tagDto1 = new TagDto();
        tagDto1.setId(1L);
        TagDto tagDto2 = new TagDto();
        tagDto2.setId(2L);
        Set<TagDto> tags = new HashSet<>(List.of(tagDto1, tagDto2));
        bookDto.setTags(tags);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(tagRepository.findById(anyLong())).thenAnswer(invocation -> {
            long tagId = invocation.getArgument(0);
            Tag tag = new Tag();
            tag.setId(tagId);
            tag.setName("Tag " + tagId);
            return Optional.of(tag);
        });
        when(bookRepository.save(any())).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L);
            return savedBook;
        });
        BookDto result = bookService.addBook(1L, bookDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("New Book Title", result.getTitle());
        assertNotNull(result.getTags());
        assertEquals(2, result.getTags().size());
    }

    @Test
    void updateNonExistentBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, this::attemptToUpdateNonExistentBook);
        verify(bookRepository, never()).save(any());
    }

    private void attemptToUpdateNonExistentBook() {
        bookService.updateBook(1L, new BookDto());
    }

    @Test
    void deleteTagFromBookWithNoTags() {
        Book book = new Book();
        book.setId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteTagFromBook(1L, 1L);
        });
        verify(bookRepository, never()).save(any());
    }

    @Test
    void getTagsByNonExistentBookId() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getTagsByBookId(1L);
        });
    }

    @Test
    void getBooksByNonExistentTagId() {
        when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBooksByTagId(1L);
        });
    }

    @Test
    void addBookWithNonExistentAuthor() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, this::attemptToAddBookWithNonExistentAuthor);
        verify(bookRepository, never()).save(any());
    }

    private void attemptToAddBookWithNonExistentAuthor() {
        bookService.addBook(1L, new BookDto());
    }

    @Test
    void convertToTagDtoTest() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Test Tag");
        TagDto result = bookService.convertToTagDto(tag);
        assertEquals(tag.getId(), result.getId());
        assertEquals(tag.getName(), result.getName());
    }

    @Test
    void addBook() {
        Long authorId = 1L;
        Long tagId = 1L;
        Author author = new Author();
        author.setId(authorId);
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        TagDto tagDto = new TagDto();
        tagDto.setId(tagId);
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(tagRepository.findById(tagId)).thenReturn(Optional.of(new Tag()));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L);
            return savedBook;
        });
        BookDto result = bookService.addBook(authorId, bookDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Book", result.getTitle());
    }

    @Test
    void findBooksByTitleContaining() {
        String keyword = "keyword";
        List<Long> cachedBookIds = List.of(1L, 2L);
        when(bookCache.getBooksFromCache(keyword)).thenReturn(cachedBookIds);
        when(bookRepository.findAllById(cachedBookIds)).thenReturn(
                List.of(new Book(), new Book()));
        List<BookDto> result = bookService.findBooksByTitleContaining(keyword);
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
