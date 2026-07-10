package com.ice.controller;

import com.ice.dto.category.CategoryPreviewResponse;
import com.ice.dto.category.CategoryTreeResponse;
import com.ice.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/tree")
    public CategoryTreeResponse tree() {
        return categoryService.getTree();
    }

    @GetMapping("/preview")
    public CategoryPreviewResponse preview() {
        return categoryService.getPreview();
    }
}
