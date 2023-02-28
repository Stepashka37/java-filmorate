package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.module.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int genId = 0;

    @GetMapping
    public List<User> getUsers() {
        log.info("Получили список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser( @Valid @RequestBody User user) throws ValidationException {
        if ( user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка валидации email");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации login");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации birthday");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (user.getName() == (null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        ++genId;
        user.setId(genId);
        log.info("Добавили пользователя с id{}", user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if ( !user.getEmail().contains("@") || user.getEmail().isBlank()) {
            log.warn("Ошибка валидации email");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка валидации login");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка валидации birthday");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (!users.containsKey(user.getId())) {
            log.warn("Ошибка проверки на наличие");
            throw new ValidationException("Пользователь с id" + user.getId() + " не найден");
        } else if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.info("Обновили данные пользователя с id{}", user.getId());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }


}
