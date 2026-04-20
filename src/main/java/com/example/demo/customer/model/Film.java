package com.example.demo.customer.model;


import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id", columnDefinition = "SMALLINT UNSIGNED")
    private Integer filmId;

    @Column(name = "title", nullable = false, length = 128)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // YEAR → map as Integer
    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "language_id", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Integer languageId;

    @Column(name = "original_language_id", columnDefinition = "TINYINT UNSIGNED")
    private Integer originalLanguageId;

    @Column(name = "rental_duration", nullable = false)
    private Integer rentalDuration;

    @Column(name = "rental_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal rentalRate;

    @Column(name = "length")
    private Integer length;

    @Column(name = "replacement_cost", nullable = false, precision = 5, scale = 2)
    private BigDecimal replacementCost;

    // ENUM → map as String
    @Column(name = "rating")
    private String rating;

    // SET → map as String (simplest approach)
    @Column(name = "special_features")
    private String specialFeatures;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // ---------------- Constructors ----------------

    public Film() {}

    public Film(Integer filmId, String title, String description, Integer releaseYear,
                Integer languageId, Integer originalLanguageId, Integer rentalDuration,
                BigDecimal rentalRate, Integer length, BigDecimal replacementCost,
                String rating, String specialFeatures, LocalDateTime lastUpdate) {

        this.filmId = filmId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.languageId = languageId;
        this.originalLanguageId = originalLanguageId;
        this.rentalDuration = rentalDuration;
        this.rentalRate = rentalRate;
        this.length = length;
        this.replacementCost = replacementCost;
        this.rating = rating;
        this.specialFeatures = specialFeatures;
        this.lastUpdate = lastUpdate;
    }


    public Integer getFilmId() { return filmId; }
    public void setFilmId(Integer filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Integer getLanguageId() { return languageId; }
    public void setLanguageId(Integer languageId) { this.languageId = languageId; }

    public Integer getOriginalLanguageId() { return originalLanguageId; }
    public void setOriginalLanguageId(Integer originalLanguageId) { this.originalLanguageId = originalLanguageId; }

    public Integer getRentalDuration() { return rentalDuration; }
    public void setRentalDuration(Integer rentalDuration) { this.rentalDuration = rentalDuration; }

    public BigDecimal getRentalRate() { return rentalRate; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }

    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }

    public BigDecimal getReplacementCost() { return replacementCost; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getSpecialFeatures() { return specialFeatures; }
    public void setSpecialFeatures(String specialFeatures) { this.specialFeatures = specialFeatures; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
