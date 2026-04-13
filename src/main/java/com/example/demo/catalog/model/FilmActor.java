package com.example.demo.catalog.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "CatalogFilmActor")
@Table(name = "film_actor")
public class FilmActor {

    @EmbeddedId
    private FilmActorId id = new FilmActorId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("actorId")
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;

    @Column(name = "last_update", nullable = false, insertable = false, updatable = false)
    private LocalDateTime lastUpdate;

    public FilmActor() {}

    public FilmActor(Actor actor, Film film) {
        this.actor = actor;
        this.film = film;
    }

    public FilmActorId getId() {
        return id;
    }

    public void setId(FilmActorId id) {
        this.id = id;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Embeddable
    public static class FilmActorId implements Serializable {
        
        @Column(name = "actor_id")
        private Long actorId;

        @Column(name = "film_id")
        private Long filmId;

        public FilmActorId() {}

        public FilmActorId(Long actorId, Long filmId) {
            this.actorId = actorId;
            this.filmId = filmId;
        }

        public Long getActorId() {
            return actorId;
        }

        public void setActorId(Long actorId) {
            this.actorId = actorId;
        }

        public Long getFilmId() {
            return filmId;
        }

        public void setFilmId(Long filmId) {
            this.filmId = filmId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FilmActorId that = (FilmActorId) o;
            return Objects.equals(actorId, that.actorId) &&
                   Objects.equals(filmId, that.filmId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(actorId, filmId);
        }
    }
}
