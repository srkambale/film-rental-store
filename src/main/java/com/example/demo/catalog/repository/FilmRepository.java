package com.example.demo.catalog.repository;

import com.example.demo.catalog.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("catalogFilmRepository")
public interface FilmRepository extends JpaRepository<Film, Long> {
}
