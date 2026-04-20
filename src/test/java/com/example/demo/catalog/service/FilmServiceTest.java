package com.example.demo.catalog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.entity.Film;
import com.example.demo.catalog.repository.FilmRepository;
import com.example.demo.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    private FilmRepository filmRepository;

    @InjectMocks
    private FilmService filmService;

    private Film film;

    @BeforeEach
    void setUp() {
        film = new Film();
        film.setFilmId(1L);
        film.setTitle("Inception");
        film.setReleaseYear(2010);
        film.setRating("PG-13");
    }

    @Test
    void testGetAllFilms() {
        when(filmRepository.findAll()).thenReturn(List.of(film));

        List<FilmSummaryDto> result = filmService.getAllFilms();

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).title());
    }

    @Test
    void testSearchFilms_TitleOnly() {
        when(filmRepository.searchFilms("Incep", null))
                .thenReturn(List.of(film));

        List<FilmDto> result = filmService.searchFilms("Incep", null);

        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).title());
    }

    @Test
    void testSearchFilms_YearOnly() {
        when(filmRepository.searchFilms(null, 2010))
                .thenReturn(List.of(film));

        List<FilmDto> result = filmService.searchFilms(null, 2010);

        assertEquals(1, result.size());
        assertEquals(2010, result.get(0).releaseYear());
    }

    @Test
    void testSearchFilms_Both() {
        when(filmRepository.searchFilms("Incep", 2010))
                .thenReturn(List.of(film));

        List<FilmDto> result = filmService.searchFilms("Incep", 2010);

        assertEquals(1, result.size());
    }

    @Test
    void testSearchFilms_None() {
        when(filmRepository.searchFilms(null, null)).thenReturn(List.of(film));

        List<FilmDto> result = filmService.searchFilms(null, null);

        assertEquals(1, result.size());
    }

    @Test
    void testGetFilmsByCategory() {
        when(filmRepository.findByCategoryName("Sci-Fi")).thenReturn(List.of(film));

        List<FilmSummaryDto> result = filmService.getFilmsByCategory("Sci-Fi");

        assertEquals(1, result.size());
    }



    @Test
    void testGetFilmsByActorName() {
        when(filmRepository.findByActorName("Leonardo")).thenReturn(List.of(film));

        List<FilmSummaryDto> result = filmService.getFilmsByActorName("Leonardo");

        assertEquals(1, result.size());
    }

    @Test
    void testGetFilmsByLanguage() {
        when(filmRepository.findByLanguageName("English")).thenReturn(List.of(film));

        List<FilmSummaryDto> result = filmService.getFilmsByLanguage("English");

        assertEquals(1, result.size());
    }

    @Test
    void testGetFilmsByRating() {
        when(filmRepository.findByRating("PG-13")).thenReturn(List.of(film));

        List<FilmSummaryDto> result = filmService.getFilmsByRating("PG-13");

        assertEquals(1, result.size());
    }


}
