package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Film;

import java.util.List;

public interface FilmsStorage {
    List<Film> getMostLikedFilms(int count);

    Film getFilm(Long id);

    List<Film> getFilms();

    void likeFilm(Long id, Long userId);

    void removeLike(Long id, Long userId);

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;


    void deleteAllFilms();


}
