package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ResponseBody;


public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);


    }

    public ValidationException() {
    }
}
