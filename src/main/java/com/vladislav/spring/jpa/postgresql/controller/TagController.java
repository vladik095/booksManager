package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public TagDto getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @PostMapping
    public void addTag(@RequestBody TagDto tagDto) {
        tagService.addTag(tagDto);
    }

    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        tagService.updateTag(id, tagDto);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }
}
