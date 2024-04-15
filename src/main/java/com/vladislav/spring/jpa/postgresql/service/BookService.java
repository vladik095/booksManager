package com.vladislav.spring.jpa.postgresql.service;

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
import java.util.List;
import java.util.Collections;
import java.util.HashSet;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class BookService {
    private static final String BOOK_NOT_FOUND_MESSAGE = "Book with id ";
    private static final String NOT_FOUND_MESSAGE = " not found";
    private static final String TAG_NOT_FOUND_MESSAGE = "Tag with id ";

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final BookCache bookCache;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, TagRepository tagRepository,
            BookCache bookCache) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.bookCache = bookCache;

    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_FOUND_MESSAGE + id));
        return convertToDto(book);
    }

    public BookDto addBook(Long authorId, BookDto bookDto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Author with id " + authorId + NOT_FOUND_MESSAGE));

        Book book = new Book();

        book.setTitle(bookDto.getTitle());
        book.setAuthor(author);

        if (bookDto.getTags() != null) {
            book.setTags(bookDto.getTags().stream()
                    .map(tagDto -> tagRepository.findById(tagDto.getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    TAG_NOT_FOUND_MESSAGE + tagDto.getId())))
                    .collect(Collectors.toSet()));
        } else {
            book.setTags(new HashSet<>());
        }
        author.getBooks().add(book);

        return convertToDto(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public void updateBook(Long id, BookDto bookDto) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        existingBook.setTitle(bookDto.getTitle());

        bookRepository.save(existingBook);
    }

    public void addTagToBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + bookId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException(TAG_NOT_FOUND_MESSAGE + tagId));

        if (book.getTags() == null) {
            book.setTags(new HashSet<>());
        }

        book.getTags().add(tag);

        bookRepository.save(book);
    }

    public Set<TagDto> getTagsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException(BOOK_NOT_FOUND_MESSAGE + bookId));

        return book.getTags().stream()
                .map(this::convertToTagDto)
                .collect(Collectors.toSet());
    }

    public Set<BookDto> getBooksByTagId(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException(TAG_NOT_FOUND_MESSAGE + tagId));

        return tag.getBooks().stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    public BookDto convertToDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());

        if (book.getTags() != null) {
            Set<TagDto> tagDtos = book.getTags().stream()
                    .map(this::convertToTagDto)
                    .collect(Collectors.toSet());
            bookDto.setTags(tagDtos);
        }

        return bookDto;
    }

    public Book convertToEntityFromDto(BookDto bookDto) {
        return convertToEntity(bookDto);
    }

    public Book convertToEntity(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        return book;
    }

    public TagDto convertToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    public void deleteTagFromBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", bookId));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId));

        if (book.getTags() != null) {
            book.removeTag(tag);
            bookRepository.save(book);
        } else {
            throw new ResourceNotFoundException("Tag", "id", tagId);
        }
    }

    public List<BookDto> findBooksByTitleContaining(String keyword) {
        List<Long> cachedBookIds = bookCache.getBooksFromCache(keyword);
        if (!cachedBookIds.isEmpty()) {
            List<Book> books = bookRepository.findAllById(cachedBookIds);
            bookCache.printCacheContents();
            return books.stream()
                    .map(this::convertToDto)
                    .toList();
        }
        List<Book> books = bookRepository.findByTitleContaining(keyword);
        if (books != null) {
            List<BookDto> bookDtos = books.stream()
                    .map(this::convertToDto)
                    .toList();
            bookCache.addToCache(keyword, bookDtos.stream().map(BookDto::getId).toList());
            return bookDtos;
        } else {
            return Collections.emptyList();
        }
    }

}
