package com.example.demo.catalog.dto;

import java.math.BigDecimal;

public record FilmSummaryDto(
    Long filmId,
    String title,
    Integer releaseYear,
    BigDecimal rentalRate,
    Integer length,
    String rating
) {}
