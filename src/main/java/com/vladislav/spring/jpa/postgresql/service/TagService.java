package com.vladislav.spring.jpa.postgresql.service;

import com.vladislav.spring.jpa.postgresql.model.Tag;
import com.vladislav.spring.jpa.postgresql.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(Long id) {
        return tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag with id " + id + " not found"));
    }

    public Tag addTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public void updateTag(Long id, Tag tag) {
        tag.setId(id);
        tagRepository.save(tag);
    }

    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
