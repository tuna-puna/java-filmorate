package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        validateFilm(film);
        film.setId(currentId++);
        films.put(film.getId(), film);
        log.info("Фильм добавлен: {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с таким ID " + film.getId() + " не найден.");
        }
        films.put(film.getId(), film);
        log.info("Обновлен фильм: {}", film);
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("название фильма пусто");
            throw new ValidationException("название фильма не должно быть пусто");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("описание фильма не должно превышать 200 символов");
            throw new ValidationException("Description must be at most 200 characters");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("дата фильма слишком ранняя");
            throw new ValidationException("дата фильма не должны быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.warn("продолжительность фильма не должна быть отрицательным числом");
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }
    }
}