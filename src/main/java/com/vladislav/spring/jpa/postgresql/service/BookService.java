package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.cache.BookCache;
import com.vladislav.spring.jpa.postgresql.dto.AuthorDto;
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
import java.util.Optional;

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

    // В методе addBook в BookService
    // В методе addBook в BookService
    public BookDto addBook(Long authorId, BookDto bookDto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Author with id " + authorId + NOT_FOUND_MESSAGE));

        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription()); // Установка описания книги
        book.setAuthor(author);

        // Добавление тегов к книге
        if (bookDto.getTags() != null) {
            Set<Tag> tags = bookDto.getTags().stream()
                    .map(tagDto -> tagRepository.findById(tagDto.getId())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    TAG_NOT_FOUND_MESSAGE + tagDto.getId())))
                    .collect(Collectors.toSet());
            book.setTags(tags);

            // Добавление тегов к автору
            author.getTags().addAll(tags);
        } else {
            book.setTags(new HashSet<>());
        }

        // Сохранение изменений в авторе в базе данных
        authorRepository.save(author);

        author.getBooks().add(book);

        return convertToDto(bookRepository.save(book));
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        // Удаление связанных тегов книги
        book.getTags().clear(); // Очищаем список тегов

        // Сохраняем изменения в базе данных
        bookRepository.save(book);

        // Удаление самой книги
        bookRepository.deleteById(id);
    }

    public void updateBook(Long id, BookDto bookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));

        existingBook.setTitle(bookDto.getTitle());
        existingBook.setDescription(bookDto.getDescription());

        // Проверяем, указан ли новый идентификатор автора
        if (bookDto.getAuthorId() != null) {
            Author author = authorRepository.findById(bookDto.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author", "id", bookDto.getAuthorId()));
            existingBook.setAuthor(author);
        }

        // Проверяем, указаны ли новые идентификаторы тегов
        if (bookDto.getTagIds() != null && !bookDto.getTagIds().isEmpty()) {
            Set<Tag> tags = bookDto.getTagIds().stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new ResourceNotFoundException("Tag", "id", tagId)))
                    .collect(Collectors.toSet());
            existingBook.setTags(tags);
        }

        bookRepository.save(existingBook);
    }

    // В методе addTagToBook в BookService
    public void addTagToBook(Long bookId, Long tagId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND_MESSAGE + bookId));
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException(TAG_NOT_FOUND_MESSAGE + tagId));

        // Добавим тег к книге
        if (book.getTags() == null) {
            book.setTags(new HashSet<>());
        }
        book.getTags().add(tag);

        // Обновим автора книги
        Author author = book.getAuthor();
        if (author != null) {
            author.getTags().add(tag); // Добавим тег автору
            authorRepository.save(author);
        }

        // Обновим книгу
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
        bookDto.setDescription(book.getDescription()); // Присвоение описания книги

        if (book.getAuthor() != null) {
            bookDto.setAuthorName(book.getAuthor().getName()); // Установка имени автора как строки
        }

        if (book.getTags() != null) {
            bookDto.setTags(book.getTags().stream()
                    .map(tag -> {
                        TagDto tagDto = new TagDto();
                        tagDto.setId(tag.getId());
                        tagDto.setName(tag.getName());
                        return tagDto;
                    })
                    .collect(Collectors.toSet()));
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

    private BookDto createOrUpdateBook(BookDto bookDto) {
        Book book = new Book(); // Initialize book with a default value
        if (bookDto.getId() != null) {
            Optional<Book> existingBookOpt = bookRepository.findById(bookDto.getId());
            if (existingBookOpt.isPresent()) {
                book = existingBookOpt.get();
                book.setTitle(bookDto.getTitle());
                book.setDescription(bookDto.getDescription());
                // Other logic for updating the book
            }
        } else {
            // Creating a new book if ID is not specified
            book.setTitle(bookDto.getTitle());
            book.setDescription(bookDto.getDescription());
        }

        // Setting the relationship with the author
        if (bookDto.getAuthorId() != null) {
            Optional<Author> authorOptional = authorRepository.findById(bookDto.getAuthorId());
            if (authorOptional.isPresent()) {
                book.setAuthor(authorOptional.get());
            }
        }

        // Setting tags
        if (bookDto.getTagIds() != null && !bookDto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(bookDto.getTagIds());
            book.setTags(new HashSet<>(tags));
        }

        // Other operations if necessary

        bookRepository.save(book);
        return convertToDto(book);
    }

    public List<BookDto> createOrUpdateBooksBulk(List<BookDto> bookList) {
        return bookList.stream()
                .map(bookDto -> createOrUpdateBook(bookDto))
                .collect(Collectors.toList());
    }

}
