package com.yumyaskova.clothesstoredatabaseapp.exceptions;

public class GoodNotFoundException extends RuntimeException{
    public GoodNotFoundException(long id) {
        super("Unable to find good with id: " + id );
    }

    public GoodNotFoundException(String name) {
        super("Unable to find good with name: " + name);
    }
}
