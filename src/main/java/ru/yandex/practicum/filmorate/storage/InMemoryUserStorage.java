package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UsersStorage {


    private Map<Long, User> users = new HashMap<>();
    private Long genId = 0L;

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }




    @Override
    public User addUser(User user) throws ValidationException {

        validateUser(user);
        ++genId;
        user.setId(genId);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        return users.get(id);
    }

    @Override
    public User updateUser(User user) throws UserNotFoundException {
        validateUser(user);
        if (!users.containsKey(user.getId())) {

            throw new UserNotFoundException("Пользователь с id" + user.getId() + " not found");
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    private void validateUser(User user){
        if ( user.getEmail().isBlank() || !user.getEmail().contains("@")) {

            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {

            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {

            throw new ValidationException("Дата рождения не может быть в будущем");
        } else if (user.getName() == (null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void deleteAllUsers(){
        users.clear();
    }


}
