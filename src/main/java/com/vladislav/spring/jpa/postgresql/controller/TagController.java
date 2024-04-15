package com.vladislav.spring.jpa.postgresql.controller;

import com.vladislav.spring.jpa.postgresql.dto.TagDto;
import com.vladislav.spring.jpa.postgresql.service.TagService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private static final Logger logger = LoggerFactory.getLogger(TagController.class);

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        logger.info("All tags fetched successfully.");
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> getTagById(@PathVariable Long id) {
        TagDto tag = tagService.getTagById(id);
        logger.info("Tag with ID {} fetched successfully.", id);
        return ResponseEntity.ok(tag);
    }

    @PostMapping
    public ResponseEntity<Void> addTag(@RequestBody TagDto tagDto) {
        tagService.addTag(tagDto);
        logger.info("Tag added successfully.");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTag(@PathVariable Long id, @RequestBody TagDto tagDto) {
        tagService.updateTag(id, tagDto);
        logger.info("Tag with ID {} updated successfully.", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        logger.info("Tag with ID {} deleted successfully.", id);
        return ResponseEntity.ok().build();
    }
}
