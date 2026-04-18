package com.example.demo.catalog.controller;

import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.dto.FilmUpdateDto;
import com.example.demo.catalog.service.FilmService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Add the unique bean name right here inside the annotation!
@RestController("catalogFilmController") 
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

    @GetMapping("/search")
    public List<FilmSummaryDto> searchFilms(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer year) {
        return filmService.searchFilms(title, year);
    }

    @GetMapping("/category/{categoryName}")
    public List<FilmSummaryDto> getFilmsByCategory(@PathVariable String categoryName) {
        return filmService.getFilmsByCategory(categoryName);
    }

    @GetMapping("/actor/{actorId}")
    public List<FilmSummaryDto> getFilmsByActor(@PathVariable Long actorId) {
        return filmService.getFilmsByActor(actorId);
    }

    @GetMapping("/actor")
    public List<FilmSummaryDto> getFilmsByActorName(@RequestParam String name) {
        return filmService.getFilmsByActorName(name);
    }

    @GetMapping("/language/{name}")
    public List<FilmSummaryDto> getFilmsByLanguage(@PathVariable String name) {
        return filmService.getFilmsByLanguage(name);
    }

    @GetMapping("/rating/{rating}")
    public List<FilmSummaryDto> getFilmsByRating(@PathVariable String rating) {
        return filmService.getFilmsByRating(rating);
    }

    @GetMapping("/{id}")
    public FilmDto getFilmById(@PathVariable Long id) {
        return filmService.getFilmById(id);
    }

    @PatchMapping("/{id}")
    public FilmDto patchFilm(@PathVariable Long id, @RequestBody FilmUpdateDto updates) {
        return filmService.patchFilm(id, updates);
    }
}