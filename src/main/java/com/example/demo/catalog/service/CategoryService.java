package com.example.demo.catalog.service;

import com.example.demo.catalog.dto.CategoryDto;
import com.example.demo.catalog.dto.CategoryUpdateDto;
import com.example.demo.catalog.entity.Category;
import com.example.demo.catalog.repository.CategoryRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> searchCategories(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.name());
        category = categoryRepository.save(category);
        return mapToDto(category);
    }

    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(dto.name());
        return mapToDto(categoryRepository.save(category));
    }

    @Transactional
    public CategoryDto patchCategory(Long id, CategoryUpdateDto updates) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (updates.getName() != null) category.setName(updates.getName());

        return mapToDto(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    private CategoryDto mapToDto(Category category) {
        return new CategoryDto(category.getCategoryId(), category.getName());
    }
}
