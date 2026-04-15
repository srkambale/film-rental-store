package com.example.demo.catalog.dto;

import java.math.BigDecimal;
import java.util.Set;

public record FilmDto(
    Long filmId,
    String title,
    String description,
    Integer releaseYear,
    Long languageId,
    Long originalLanguageId,
    Integer rentalDuration,
    BigDecimal rentalRate,
    Integer length,
    BigDecimal replacementCost,
    String rating,
    String specialFeatures,
    Set<ActorDto> actors,
    Set<CategoryDto> categories
) {}
