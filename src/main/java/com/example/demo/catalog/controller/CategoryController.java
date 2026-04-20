package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.CategoryDto;
import com.example.demo.catalog.dto.CategoryUpdateDto;
import com.example.demo.catalog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("catalogCategoryController")
@RequestMapping("/api/v1/catalog/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/search")
    public List<CategoryDto> searchCategories(@RequestParam String name) {
        return categoryService.searchCategories(name);
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategory(id, categoryDto);
    }

    @PatchMapping("/{id}")
    public CategoryDto patchCategory(@PathVariable Long id, @RequestBody CategoryUpdateDto updates) {
        return categoryService.patchCategory(id, updates);
    }


}
