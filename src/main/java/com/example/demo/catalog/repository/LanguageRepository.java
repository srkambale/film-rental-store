package com.example.demo.catalog.repository;

import com.example.demo.catalog.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("catalogLanguageRepository")
public interface LanguageRepository extends JpaRepository<Language, Long> {
}
