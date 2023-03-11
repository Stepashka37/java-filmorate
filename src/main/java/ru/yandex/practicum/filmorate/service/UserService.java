package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.module.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UsersStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private UsersStorage storage;

    @Autowired
    public UserService(UsersStorage storage) {
        this.storage = storage;
    }

    public List<User> getUsers(){
       return storage.getUsers();
    }

    public User addUser(User user){
        return storage.addUser(user);
    }

    public User updateUser(User user){
        return storage.updateUser(user);
    }

    public User getUser(Long id) {
        return storage.getUser(id);
    }

    public void addFriend(Long id, Long idToAdd){
        /*if (storage.getUser(id) == null) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        if (storage.getUser(idToAdd) == null) {
            throw new UserNotFoundException("Пользователь с id " + idToAdd + " не найден");
        }*/
        User user = storage.getUser(id);
        User userToAdd = storage.getUser(idToAdd);
       user.addFriend(idToAdd);
       userToAdd.addFriend(id);

    }

    public void deleteFriend(Long id, Long idToDelete){
        /*if (storage.getUser(id) == null) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        if (storage.getUser(idToDelete) == null) {
            throw new UserNotFoundException("Пользователь с id " + idToDelete + " не найден");
        }*/
        User user = storage.getUser(id);
        User userToDelete = storage.getUser(idToDelete);
        user.deleteFriend(idToDelete);
        userToDelete.deleteFriend(id);
    }

    public List<User> showFriends(Long id) {
        List<User> result = new ArrayList<>();
        /*if (storage.getUser(id) == null) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }*/
        User user = storage.getUser(id);
        if (user.getFriends().size() == 0) {
            return new ArrayList<>();
        }
        for (Long idUser : user.getFriends()) {
            result.add(storage.getUser(idUser));

        }
        return result;

    }

    public List<User> showCommonFriends(Long idNumb1, Long idNumb2) {
        List<User> result = new ArrayList<>();
        /*if (storage.getUser(idNumb1) == null) {
            throw new UserNotFoundException("Пользователь с id " + idNumb1 + " не найден");
        }
        if (storage.getUser(idNumb2) == null) {
            throw new UserNotFoundException("Пользователь с id " + idNumb2 + " не найден");
        }*/
        User userNumb1 = storage.getUser(idNumb1);
        User userNumb2 = storage.getUser(idNumb2);
        if (userNumb1.getFriends() == null || userNumb2.getFriends() == null) {
            return new ArrayList<>();
        }
        Set<Long> intersectSet = new HashSet<>(userNumb1.getFriends());
        intersectSet.retainAll(userNumb2.getFriends());


        for (Long id : intersectSet) {
             result.add(storage.getUser(id));

        }
        return result;

    }
}
