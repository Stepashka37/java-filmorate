package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;

import java.util.List;


public interface UsersStorage {
    List<User> getUsers();

    User addUser(User user) throws ValidationException;

    User updateUser(User user);

    void deleteAllUsers();


    User getUser(Long id);

}