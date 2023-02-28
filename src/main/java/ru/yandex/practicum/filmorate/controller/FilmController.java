package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int genId = 0;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получили список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {

        validateFilm(film);
        ++genId;
        film.setId(genId);
        films.put(film.getId(), film);
        log.info("Добавили фильм с id{}", film.getId());
        return films.get(film.getId());
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
         validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Ошибка проверки на наличие");
            throw new ValidationException("Фильм с id" + film.getId() + " не найден");
        }
        log.info("Обновили фильм с id{}", film.getId());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank() ) {
            log.warn("Ошибка валидации name");
            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.warn("Ошибка валидации description");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации release date");
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            log.warn("Ошибка валидации duration");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    public void cleanFilms(){
        films.clear();
    }
}
