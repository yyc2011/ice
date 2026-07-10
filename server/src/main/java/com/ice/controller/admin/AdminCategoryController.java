package com.ice.controller.admin;

import com.ice.dto.admin.AdminCategoryDto;
import com.ice.dto.admin.AdminCategoryRequest;
import com.ice.service.admin.AdminCategoryService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/categories")
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @GetMapping
    public List<AdminCategoryDto> list() {
        return adminCategoryService.list();
    }

    @PostMapping
    public AdminCategoryDto create(@RequestBody AdminCategoryRequest request) {
        return adminCategoryService.create(request);
    }

    @PutMapping("/{id}")
    public AdminCategoryDto update(@PathVariable long id, @RequestBody AdminCategoryRequest request) {
        return adminCategoryService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        adminCategoryService.delete(id);
    }
}
