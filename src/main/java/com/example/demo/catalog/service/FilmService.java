package com.example.demo.catalog.service;

import com.example.demo.catalog.dto.ActorDto;
import com.example.demo.catalog.dto.CategoryDto;
import com.example.demo.catalog.dto.FilmDto;
import com.example.demo.catalog.dto.FilmSummaryDto;
import com.example.demo.catalog.dto.FilmUpdateDto;
import com.example.demo.catalog.entity.Film;
import com.example.demo.catalog.repository.FilmRepository;
import com.example.demo.catalog.repository.LanguageRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;

    public FilmService(FilmRepository filmRepository, LanguageRepository languageRepository) {
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
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

    @Transactional
    public FilmDto patchFilm(Long id, FilmUpdateDto updates) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Film not found"));

        if (updates.getTitle() != null) film.setTitle(updates.getTitle());
        if (updates.getDescription() != null) film.setDescription(updates.getDescription());
        if (updates.getReleaseYear() != null) film.setReleaseYear(updates.getReleaseYear());
        if (updates.getRentalDuration() != null) film.setRentalDuration(updates.getRentalDuration());
        if (updates.getRentalRate() != null) film.setRentalRate(updates.getRentalRate());
        if (updates.getLength() != null) film.setLength(updates.getLength());
        if (updates.getReplacementCost() != null) film.setReplacementCost(updates.getReplacementCost());
        if (updates.getRating() != null) film.setRating(updates.getRating());
        if (updates.getSpecialFeatures() != null) film.setSpecialFeatures(updates.getSpecialFeatures());

        if (updates.getLanguageId() != null) {
            film.setLanguage(languageRepository.findById(updates.getLanguageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Language not found with id " + updates.getLanguageId())));
        }

        if (updates.getOriginalLanguageId() != null) {
            film.setOriginalLanguage(languageRepository.findById(updates.getOriginalLanguageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Original Language not found with id " + updates.getOriginalLanguageId())));
        }

        return mapToDto(filmRepository.save(film));
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
