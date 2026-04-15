package com.example.demo.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import com.example.demo.customer.model.Film;
import com.example.demo.customer.repository.FilmRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class FilmServiceImplTest {

    @InjectMocks
    private FilmServiceImpl filmService;

    @Mock
    private FilmRepository filmRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFilmsByTitle() {
        Film film = new Film();
        film.setFilmId(1);
        film.setTitle("Inception");

        when(filmRepository.searchByTitle("Inception"))
                .thenReturn(List.of(film));

        assertEquals(1, filmService.searchFilmsByTitle("Inception").size());
    }
}