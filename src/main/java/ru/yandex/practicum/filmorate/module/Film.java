package ru.yandex.practicum.filmorate.module;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
public class Film {
    private int id;
    @NotNull
    private String name;
    @Size(min = 0, max = 200)
    private String description;
    @Past
    @NonNull
    private LocalDate releaseDate;
    @Positive
    @NonNull
    private long duration;



    public Film(int id, String name, String description, LocalDate releaseDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id == film.id && duration == film.duration && name.equals(film.name) && description.equals(film.description) && releaseDate.equals(film.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                '}';
    }

   /* public static class FilmBuilder {
        private int id;
        @NotNull
        private String name;
        @Size(min = 0, max = 200)
        private String description;
        @Past
        @NotNull
        private LocalDate releaseDate;
        @Positive
        @NotNull
        private long duration;



        public FilmBuilder() {
        }

        public FilmBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public FilmBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public FilmBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public FilmBuilder setReleaseDate(LocalDate releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public FilmBuilder setDuration(long duration) {
            this.duration = duration;
            return this;
        }

        public Film build() {
            return new Film(id, name, description, releaseDate, duration);
        }*/
    }

