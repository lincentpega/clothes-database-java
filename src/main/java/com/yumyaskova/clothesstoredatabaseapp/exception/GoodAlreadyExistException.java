package com.yumyaskova.clothesstoredatabaseapp.exception;

public class GoodAlreadyExistException extends RuntimeException {
    public GoodAlreadyExistException(String message) {
        super(message);
    }

    public GoodAlreadyExistException(Long id) {
        super("Good with id " + id + " already exist");
    }
}
