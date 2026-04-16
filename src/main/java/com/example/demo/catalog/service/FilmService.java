package com.example.demo.catalog.service;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.dto.CategoryDto;
import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.entity.Film;
import com.example.demo.catalog.repository.FilmRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<FilmSummaryDto> searchFilms(String title, Integer year) {
        List<Film> films;
        if (title != null && year != null) {
            films = filmRepository.findByTitleContainingIgnoreCaseAndReleaseYear(title, year);
        } else if (title != null) {
            films = filmRepository.findByTitleContainingIgnoreCase(title);
        } else if (year != null) {
            films = filmRepository.findByReleaseYear(year);
        } else {
            films = filmRepository.findAll();
        }
        return films.stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmSummaryDto> getFilmsByCategory(String categoryName) {
        return filmRepository.findByCategoryName(categoryName).stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmSummaryDto> getFilmsByActor(Long actorId) {
        return filmRepository.findByActorId(actorId).stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmSummaryDto> getFilmsByActorName(String name) {
        return filmRepository.findByActorName(name).stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmSummaryDto> getFilmsByLanguage(String languageName) {
        return filmRepository.findByLanguageName(languageName).stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FilmSummaryDto> getFilmsByRating(String rating) {
        return filmRepository.findByRating(rating).stream()
                .map(this::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FilmDto getFilmById(Long id) {
        return filmRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found"));
    }

    private FilmSummaryDto mapToSummaryDto(Film film) {
        return new FilmSummaryDto(
                film.getFilmId(),
                film.getTitle(),
                film.getReleaseYear(),
                film.getRentalRate(),
                film.getLength(),
                film.getRating());
    }

    private FilmDto mapToDto(Film film) {
        Set<ActorDto> actors = film.getFilmActors() == null ? Set.of()
                : film.getFilmActors().stream()
                        .map(fa -> new ActorDto(
                                fa.getActor().getActorId(),
                                fa.getActor().getFirstName(),
                                fa.getActor().getLastName()))
                        .collect(Collectors.toSet());

        Set<CategoryDto> categories = film.getFilmCategories() == null ? Set.of()
                : film.getFilmCategories().stream()
                        .map(fc -> new CategoryDto(
                                fc.getCategory().getCategoryId(),
                                fc.getCategory().getName()))
                        .collect(Collectors.toSet());

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
                actors,
                categories);
    }
}
