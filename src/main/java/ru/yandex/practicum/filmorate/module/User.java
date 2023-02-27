package ru.yandex.practicum.filmorate.module;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class User {
    private int id;
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
                "id=" + id +
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
        return id == user.id && email.equals(user.email) && login.equals(user.login) && name.equals(user.name) && birthday.equals(user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, name, birthday);
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public static class UserBuilder {
        private int id;
        @Email
        private String email;
        @NotNull
        private String login;
        private String name;
        @PastOrPresent
        private LocalDate birthday;

        public UserBuilder() {
        }

        public User.UserBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public User.UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public User.UserBuilder setLogin(String login) {
            this.login = login;
            return this;
        }

        public User.UserBuilder setBirthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public User.UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public User build() {
            return new User(id,email,login, name,birthday);
        }
    }
}
