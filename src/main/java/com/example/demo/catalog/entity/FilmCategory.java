package com.example.demo.catalog.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "CatalogFilmCategory")
@Table(name = "film_category")
public class FilmCategory {

    @EmbeddedId
    private FilmCategoryId id = new FilmCategoryId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private LocalDateTime lastUpdate;

    public FilmCategory() {}

    public FilmCategory(Film film, Category category) {
        this.film = film;
        this.category = category;
    }

    public FilmCategoryId getId() {
        return id;
    }

    public void setId(FilmCategoryId id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Embeddable
    public static class FilmCategoryId implements Serializable {

        @Column(name = "film_id")
        private Long filmId;

        @Column(name = "category_id")
        private Long categoryId;

        public FilmCategoryId() {}

        public FilmCategoryId(Long filmId, Long categoryId) {
            this.filmId = filmId;
            this.categoryId = categoryId;
        }

        public Long getFilmId() {
            return filmId;
        }

        public void setFilmId(Long filmId) {
            this.filmId = filmId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilmCategoryId that = (FilmCategoryId) o;
            return Objects.equals(filmId, that.filmId) &&
                   Objects.equals(categoryId, that.categoryId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(filmId, categoryId);
        }
    }
}
