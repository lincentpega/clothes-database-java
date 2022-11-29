package com.yumyaskova.clothesstoredatabaseapp.controller;

import com.yumyaskova.clothesstoredatabaseapp.exceptions.GoodNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GoodNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(GoodNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String goodNotFoundHandler(GoodNotFoundException exception) {
        return exception.getMessage();
    }
}
