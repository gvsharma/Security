package com.security.security.Controllers;

import com.security.security.entities.TagEntity;
import com.security.security.services.TagsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TagsController {
    private final TagsService tags;

    public TagsController(TagsService tags) {
        this.tags = tags;
    }

    @GetMapping("/tags")
    ResponseEntity<List<TagEntity>> getTags() {
        return ResponseEntity.ok(tags.getAllTags());
    }
}
