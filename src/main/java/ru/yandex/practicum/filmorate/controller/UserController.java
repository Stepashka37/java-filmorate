package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

   private UserService usersService;

   @Autowired
    public UserController(UserService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получили список всех пользователей");
       return usersService.getUsers();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser( @Valid @RequestBody User user) throws ValidationException {

        log.info("Добавили пользователя с id{}", user.getId());
        return usersService.addUser(user);

    }

    @PutMapping()
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Обновили данные пользователя с id{}", user.getId());
        return usersService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
       /*if (id <= 0 || friendId <= 0) {
           throw new ValidationException("id должен быть больше или равен 0");
       }
       if (id == null || friendId == null) {
           throw new ValidationException("не указан id");
       }*/
       usersService.addFriend(id, friendId);
       log.info("Пользователь с id " + friendId + " добавился в друзья к пользователю с id " + id);
        System.out.println(usersService.showFriends(id).size());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new ValidationException("id должен быть больше или равен 0");
        }
        if (id == null || friendId == null) {
            throw new ValidationException("не указан id");
        }
       usersService.deleteFriend(id, friendId);
        log.info("Пользователь с id " + friendId + " удалил из друзей пользователя с id " + id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
       return usersService.showFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
       return usersService.showCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        if (id <= 0) {
            throw new ValidationException("id должен быть больше или равен 0");
        }
        if (id == null) {
            throw new ValidationException("не указан id");
        }
       return usersService.getUser(id);
    }




}
