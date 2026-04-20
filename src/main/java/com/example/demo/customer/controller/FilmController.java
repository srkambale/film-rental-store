package com.example.demo.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.customer.dto.FilmSearchDto;
import com.example.demo.customer.service.FilmService;

@CrossOrigin(origins = "http://10.30.74.131:8082")
@RestController
@RequestMapping("/api/v1/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/search/title")
    public List<FilmSearchDto> searchByTitle(@RequestParam String title) {
        return filmService.searchFilmsByTitle(title);
    }

    @GetMapping("/search/actor")
    public List<FilmSearchDto> searchByActor(@RequestParam String actor) {
        return filmService.searchFilmsByActor(actor);
    }

    @GetMapping("/search/category")
    public List<FilmSearchDto> searchByCategory(@RequestParam String category) {
        return filmService.searchFilmsByCategory(category);
    }
}
