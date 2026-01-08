package com.store.book.exception;

public class RegistrationException extends RuntimeException { // Краще RuntimeException
    public RegistrationException(String message) {
        super(message);
    }
}
