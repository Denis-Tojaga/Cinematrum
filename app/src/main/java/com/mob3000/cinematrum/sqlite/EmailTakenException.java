package com.mob3000.cinematrum.sqlite;

public class EmailTakenException extends Exception {
    public EmailTakenException(String errorMessage) {
        super(errorMessage);
    }
}

