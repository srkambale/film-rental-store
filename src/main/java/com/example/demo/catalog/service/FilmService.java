package com.example.demo.catalog.service;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.dto.CategoryDto;
import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.entity.Film;
import com.example.demo.catalog.repository.FilmRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Transactional(readOnly = true)
    public List<FilmSummaryDto> getAllFilms() {
        return filmRepository.findAll().stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FilmDto getFilmById(Long id) {
        return filmRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found"));
    }

    private FilmSummaryDto mapToSummaryDto(Film film) {
        return new FilmSummaryDto(
                film.getFilmId(),
                film.getTitle(),
                film.getReleaseYear(),
                film.getRentalRate(),
                film.getLength(),
                film.getRating()
        );
    }

    private FilmDto mapToDto(Film film) {
        return new FilmDto(
                film.getFilmId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseYear(),
                film.getLanguage() != null ? film.getLanguage().getLanguageId() : null,
                film.getOriginalLanguage() != null ? film.getOriginalLanguage().getLanguageId() : null,
                film.getRentalDuration(),
                film.getRentalRate(),
                film.getLength(),
                film.getReplacementCost(),
                film.getRating(),
                film.getSpecialFeatures(),
                Set.of(),
                Set.of()
        );
    }
}
