package com.filmrental.catalog.dto;

import java.math.BigDecimal;

public class FilmSummaryDto {

    private Integer filmId;
    private String title;
    private Integer rentalDuration;
    private BigDecimal rentalRate;
    private BigDecimal replacementCost;

    public FilmSummaryDto() {}

    public FilmSummaryDto(Integer filmId, String title, Integer rentalDuration,
                          BigDecimal rentalRate, BigDecimal replacementCost) {
        this.filmId = filmId;
        this.title = title;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.replacementCost = replacementCost;
    }

    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getRentalDuration() { return rentalDuration; }
    public void setRentalDuration(Integer rentalDuration) { this.rentalDuration = rentalDuration; }

    public BigDecimal getRentalRate() { return rentalRate; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }

    public BigDecimal getReplacementCost() { return replacementCost; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }
}