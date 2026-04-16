package com.example.demo.customer.controller;

import com.example.demo.customer.dto.FilmSearchDto;
import com.example.demo.customer.service.FilmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmControllerTest {

    @Mock
    private FilmService filmService;

    @InjectMocks
    private FilmController filmController;

    private FilmSearchDto searchDto;

    @BeforeEach
    void setUp() {
        searchDto = new FilmSearchDto();
        searchDto.setTitle("Academy Dinosaur");
    }

    @Test
    void testSearchByTitle() {
        List<FilmSearchDto> list = Arrays.asList(searchDto);
        when(filmService.searchFilmsByTitle("Academy")).thenReturn(list);

        List<FilmSearchDto> result = filmController.searchByTitle("Academy");

        assertEquals(1, result.size());
        assertEquals("Academy Dinosaur", result.get(0).getTitle());
        verify(filmService, times(1)).searchFilmsByTitle("Academy");
    }

    @Test
    void testSearchByActor() {
        List<FilmSearchDto> list = Arrays.asList(searchDto);
        when(filmService.searchFilmsByActor("PENELOPE GUINESS")).thenReturn(list);

        List<FilmSearchDto> result = filmController.searchByActor("PENELOPE GUINESS");

        assertEquals(1, result.size());
        verify(filmService, times(1)).searchFilmsByActor("PENELOPE GUINESS");
    }

    @Test
    void testSearchByCategory() {
        List<FilmSearchDto> list = Arrays.asList(searchDto);
        when(filmService.searchFilmsByCategory("Action")).thenReturn(list);

        List<FilmSearchDto> result = filmController.searchByCategory("Action");

        assertEquals(1, result.size());
        verify(filmService, times(1)).searchFilmsByCategory("Action");
    }
}
