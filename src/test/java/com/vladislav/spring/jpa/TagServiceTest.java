package com.vladislav.spring.jpa;

import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.model.Tag;
import com.vladislav.spring.jpa.postgresql.repository.TagRepository;
import com.vladislav.spring.jpa.postgresql.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllTags() {
        // Arrange
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setName("Tag 1");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setName("Tag 2");

        when(tagRepository.findAll()).thenReturn(List.of(tag1, tag2));

        // Act
        List<TagDto> tags = tagService.getAllTags();

        // Assert
        assertEquals(2, tags.size());
        assertEquals("Tag 1", tags.get(0).getName());
        assertEquals("Tag 2", tags.get(1).getName());
    }

    @Test
    void getTagById() {
        // Arrange
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        // Act
        TagDto tagDto = tagService.getTagById(1L);

        // Assert
        assertEquals("Tag", tagDto.getName());
    }

    @Test
    void addTag() {
        // Arrange
        TagDto tagDto = new TagDto();
        tagDto.setName("New Tag");

        Tag savedTag = new Tag();
        savedTag.setId(1L);
        savedTag.setName("New Tag");

        when(tagRepository.save(any(Tag.class))).thenReturn(savedTag);

        // Act
        tagService.addTag(tagDto);

        // Assert
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void updateTag() {
        // Arrange
        TagDto tagDto = new TagDto();
        tagDto.setId(1L);
        tagDto.setName("Updated Tag");

        Tag existingTag = new Tag();
        existingTag.setId(1L);
        existingTag.setName("Tag");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(existingTag));

        // Act
        tagService.updateTag(1L, tagDto);

        // Assert
        verify(tagRepository, times(1)).findById(1L);
        verify(tagRepository, times(1)).save(any(Tag.class));
    }

    @Test
    void deleteTag() {
        // Arrange
        Long tagId = 1L;

        // Act
        tagService.deleteTag(tagId);

        // Assert
        verify(tagRepository, times(1)).deleteById(tagId);
    }
    // Тесты для остальных методов могут быть реализованы аналогичным образом.
}
