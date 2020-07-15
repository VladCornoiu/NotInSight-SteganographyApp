package com.example.restservice.exception;

public class NoHiddenMessageException extends RuntimeException {
    public NoHiddenMessageException(String message) {
        super(message);
    }

    public NoHiddenMessageException(String message, Throwable cause) {
        super(message, cause);
    }
}
