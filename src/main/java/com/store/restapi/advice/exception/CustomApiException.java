package com.store.restapi.advice.exception;

public class CustomApiException extends RuntimeException {

    public CustomApiException() {
        super();
    }

    public CustomApiException(String message) {
        super(message);
    }

    public CustomApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
