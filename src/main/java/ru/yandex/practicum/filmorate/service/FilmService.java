package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.module.Film;
import ru.yandex.practicum.filmorate.storage.FilmsStorage;
import ru.yandex.practicum.filmorate.storage.UsersStorage;

import java.util.List;
@Service
public class FilmService {
    //private UsersStorage usersStorage;
    private FilmsStorage filmsStorage;
    @Autowired
    public FilmService(FilmsStorage filmsStorage) {
        this.filmsStorage = filmsStorage;
    }

    /*@Autowired
    public FilmService(UsersStorage usersStorage) {
        this.usersStorage = usersStorage;
    }*/

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
    filmsStorage.likeFilm(id,userId);
    }

    public void removeLike(Long id, Long userId) {
        filmsStorage.removeLike(id, userId);
    }

    public List<Film> getMostLikedFilms(int count) {
        return filmsStorage.getMostLikedFilms(count);
    }

    public Film getFilm(Long id) {
        return filmsStorage.getFilm(id);
    }


}
