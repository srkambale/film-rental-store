package com.example.demo.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.catalog.entity.Film;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository("catalogFilmRepository")
public interface FilmRepository extends JpaRepository<Film, Long> {
    @Query("SELECT DISTINCT f FROM CatalogFilm f " +
           "LEFT JOIN FETCH f.filmActors " +
           "LEFT JOIN FETCH f.filmCategories " +
           "LEFT JOIN FETCH f.language " +
           "WHERE (COALESCE(:title, '') = '' OR LOWER(f.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
           "AND (:year IS NULL OR f.releaseYear = :year)")
    List<Film> searchFilms(@Param("title") String title, @Param("year") Integer year);
    
    @Query("SELECT fc.film FROM CatalogFilmCategory fc WHERE fc.category.name = :categoryName")
    List<Film> findByCategoryName(@Param("categoryName") String categoryName);



    @Query("SELECT fa.film FROM CatalogFilmActor fa WHERE " +
           "LOWER(fa.actor.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR " +
           "LOWER(fa.actor.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Film> findByActorName(@Param("name") String name);

    @Query("SELECT f FROM CatalogFilm f WHERE f.language.name = :languageName")
    List<Film> findByLanguageName(@Param("languageName") String languageName);

    List<Film> findByRating(String rating);
}
