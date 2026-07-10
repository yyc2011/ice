package com.ice.controller;

import com.ice.auth.AuthContext;
import com.ice.dto.tag.CreateTagRequest;
import com.ice.dto.tag.TagItemDto;
import com.ice.dto.tag.TagListResponse;
import com.ice.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/search")
    public TagListResponse search(@RequestParam(name = "q", required = false) String q) {
        return tagService.search(q);
    }

    @PostMapping
    public TagItemDto create(@RequestBody CreateTagRequest request) {
        AuthContext.requireUserId();
        return tagService.create(request);
    }
}
