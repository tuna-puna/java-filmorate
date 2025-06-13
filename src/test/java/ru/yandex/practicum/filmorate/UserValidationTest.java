package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTest {

    private final UserController controller = new UserController();

    @Test
    void shouldThrowWhenEmailInvalid() {
        User user = new User();
        user.setEmail("неправильный email");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    void shouldThrowWhenLoginContainsSpace() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("lo gin");
        user.setName("name");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }

    @Test
    void shouldUseLoginAsNameIfEmpty() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login123");
        user.setName("");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        User created = controller.createUser(user);
        assertEquals("login123", created.getName());
    }

    @Test
    void shouldThrowWhenBirthdayInFuture() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("login123");
        user.setName("name");
        user.setBirthday(LocalDate.now().plusDays(1));

        assertThrows(ValidationException.class, () -> controller.createUser(user));
    }
}