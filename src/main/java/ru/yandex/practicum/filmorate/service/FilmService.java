package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.UsersStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmsStorage filmsStorage;
    @Autowired
    public FilmService(FilmsStorage filmsStorage) {
        this.filmsStorage = filmsStorage;
    }



    public List<Film> getFilms() {
        return filmsStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmsStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmsStorage.updateFilm(film);
    }

    public void likeFilm(Long id, Long userId) {
        Film film = filmsStorage.getFilm(id);
        film.likeFilm(userId);
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmsStorage.getFilm(id);
        film.removeLike(userId);

    }

    public List<Film> getMostLikedFilms(int count) {
        if (filmsStorage.getFilms().isEmpty()) {
            return new ArrayList<>();
        }
        if (filmsStorage.getFilms().size() <= count) {
            return new ArrayList<>(filmsStorage.getFilms());
        }
        List<Film> values = filmsStorage.getFilms().stream()
                .sorted((e1, e2) ->
        Integer.compare(e1.getLikesNumber(), e2.getLikesNumber()))
                .collect(Collectors.toList());

        Collections.reverse(values);
        return values.subList(0, count);
    }

    public Film getFilm(Long id) {
        return filmsStorage.getFilm(id);
    }

    public void deleteFilm(Long id) {
        filmsStorage.deleteFilm(id);
    }

    public void deleteAllFilms(){
        filmsStorage.deleteAllFilms();
    }

}
