package com.yumyaskova.clothesstoredatabaseapp.exception;

public class GoodNotFoundException extends RuntimeException{

    public GoodNotFoundException(String message) {
        super(message);
    }
    public GoodNotFoundException(long id) {
        super("Unable to find good with id: " + id );
    }
}
