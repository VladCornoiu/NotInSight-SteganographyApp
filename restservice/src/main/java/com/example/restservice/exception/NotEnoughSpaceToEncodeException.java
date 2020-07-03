package com.example.restservice.exception;

public class NotEnoughSpaceToEncodeException extends RuntimeException {
    public NotEnoughSpaceToEncodeException(String message) {
        super(message);
    }

    public NotEnoughSpaceToEncodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
