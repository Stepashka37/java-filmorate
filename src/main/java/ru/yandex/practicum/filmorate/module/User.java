package ru.yandex.practicum.filmorate.module;


import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.Value;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class User {
@Singular
     private Set<Long> friends;
    private Long id;
    @Email
    private String email;
    @NotNull
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;


    @Override
    public String toString() {
        return "User{" +
                "friends=" + friends +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(friends, user.friends) && Objects.equals(email, user.email) && Objects.equals(login, user.login) && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(friends, id, email, login, name, birthday);
    }

    public Set<Long> getFriends() {
        return friends;
    }

    public void addFriend(Long id) {
        friends.add(id);
    }

    public void deleteFriend(Long id) {
        friends.remove(id);
    }
}
