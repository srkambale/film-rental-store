package com.example.demo.customer.repository;

import com.example.demo.customer.model.Film;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Film> searchByTitle(String title);

    @Query("""
        SELECT f FROM Film f
        JOIN f.filmActors fa
        JOIN fa.actor a
        WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :actor, '%'))
           OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :actor, '%'))
    """)
    List<Film> searchByActor(String actor);

    @Query("""
        SELECT f FROM Film f
        JOIN f.filmCategories fc
        JOIN fc.category c
        WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :category, '%'))
    """)
    List<Film> searchByCategory(String category);
}
