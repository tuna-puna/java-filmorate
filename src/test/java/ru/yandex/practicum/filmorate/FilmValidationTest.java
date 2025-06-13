package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTest {

    private final FilmController controller = new FilmController();

    @Test
    void shouldThrowWhenNameIsEmpty() {
        Film film = new Film();
        film.setName("");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> controller.addFilm(film)
        );
        assertEquals("название фильма не должно быть пустым", exception.getMessage());
    }

    @Test
    void shouldThrowWhenDescriptionTooLong() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldThrowWhenReleaseDateTooEarly() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(120);

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldThrowWhenDurationNonPositive() {
        Film film = new Film();
        film.setName("Test");
        film.setDescription("desc");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }
}