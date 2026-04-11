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

    @Query(value = """
        SELECT f.* FROM film f
        JOIN film_actor fa ON f.film_id = fa.film_id
        JOIN actor a ON fa.actor_id = a.actor_id
        WHERE LOWER(a.first_name) LIKE LOWER(CONCAT('%', :actor, '%'))
           OR LOWER(a.last_name) LIKE LOWER(CONCAT('%', :actor, '%'))
    """, nativeQuery = true)
    List<Film> searchByActor(String actor);

    @Query(value = """
        SELECT f.* FROM film f
        JOIN film_category fc ON f.film_id = fc.film_id
        JOIN category c ON fc.category_id = c.category_id
        WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :category, '%'))
    """, nativeQuery = true)
    List<Film> searchByCategory(String category);
}
