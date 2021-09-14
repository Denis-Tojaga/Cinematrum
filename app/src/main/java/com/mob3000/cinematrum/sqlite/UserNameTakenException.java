package com.mob3000.cinematrum.sqlite;

public class UserNameTakenException extends Exception {
    public UserNameTakenException(String errorMessage) {
        super(errorMessage);
    }
}

