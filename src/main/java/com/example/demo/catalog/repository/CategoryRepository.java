package com.example.demo.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.catalog.entity.Category;

import java.util.List;

@Repository("catalogCategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContainingIgnoreCase(String name);
}
