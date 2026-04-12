package com.example.demo.catalog.repository;

import com.example.demo.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("catalogCategoryRepository")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
