package com.filmrental.catalog.repository;

import com.filmrental.catalog.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    Page<Film> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Film> findByRating(Film.Rating rating, Pageable pageable);

    Page<Film> findByLanguageLanguageId(Integer languageId, Pageable pageable);

    @Query("""
        SELECT f FROM Film f
        JOIN f.categories c
        WHERE LOWER(c.name) = LOWER(:categoryName)
    """)
    Page<Film> findByCategoryName(@Param("categoryName") String categoryName, Pageable pageable);

    @Query("""
        SELECT f FROM Film f
        JOIN f.actors a
        WHERE a.actorId = :actorId
    """)
    Page<Film> findByActorId(@Param("actorId") Integer actorId, Pageable pageable);

    @Query("""
        SELECT f FROM Film f
        JOIN f.categories c
        WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))
        AND LOWER(c.name) = LOWER(:categoryName)
    """)
    Page<Film> findByTitleAndCategory(
        @Param("title") String title,
        @Param("categoryName") String categoryName,
        Pageable pageable
    );
}