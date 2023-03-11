package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.Film;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmsStorage {
    private Map<Long, Film> films = new HashMap<>();
    private Long genId = 0L;

    @Override
    public List<Film> getMostLikedFilms(int count) {
        if (films.isEmpty()) {
            return new ArrayList<>();
        }
        if (films.size() <= count) {
            return new ArrayList<>(films.values());
        }
        Map<Long,Film> sortedNewMap = films.entrySet().stream().sorted((e1,e2) ->
                        Integer.compare(e2.getValue().getLikesNumber(), e1.getValue().getLikesNumber()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
        ArrayList<Film> values = new ArrayList<>(sortedNewMap.values());
        //Collections.reverse(values);
        return values.subList(0, count);

        }

        @Override
        public Film getFilm(Long id){
            if (!films.containsKey(id)) {
                throw new FilmNotFoundException("Фильм с id " + id + " не найден");
            }
        return films.get(id);
        }


    @Override
    public List<Film> getFilms(){
        return new ArrayList<>(films.values());
    }

    @Override
    public void likeFilm(Long id, Long userId) {
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");

        }
        films.get(id).likeFilm(userId);
    }

    @Override
    public void removeLike(Long id, Long userId){
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм с id " + id + " не найден");

        }
        films.get(id).removeLike(userId);
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        validateFilm(film);
        ++genId;
        film.setId(genId);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        films.put(film.getId(), film);
        return films.get(film.getId());
    }

    @Override
    public void deleteAllFilms(){
        films.clear();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank() ) {
            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) || film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }


}
