package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.service.FilmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmSummaryDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }
}
