package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.model.Tag;
import com.vladislav.spring.jpa.postgresql.repository.TagRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDto> getAllTags() {
        return tagRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    public TagDto getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag with id " + id + " not found"));
        return convertToDto(tag);
    }

    public void addTag(TagDto tagDto) {
        Tag tag = convertToEntity(tagDto);
        tagRepository.save(tag);
    }

    public void updateTag(Long id, TagDto tagDto) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag with id " + id + " not found"));
        Tag updatedTag = convertToEntity(tagDto);
        updatedTag.setId(existingTag.getId());
        tagRepository.save(updatedTag);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    private TagDto convertToDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    private Tag convertToEntity(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());
        return tag;
    }

}
