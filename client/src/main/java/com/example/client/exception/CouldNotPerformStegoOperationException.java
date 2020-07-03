package com.example.client.exception;

public class CouldNotPerformStegoOperationException extends RuntimeException {
    private String errorMessage;

    public CouldNotPerformStegoOperationException(String errorMessage) {
        super(errorMessage);
    }
}
