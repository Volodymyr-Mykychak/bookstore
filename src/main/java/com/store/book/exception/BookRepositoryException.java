package com.store.book.exception;

public class BookRepositoryException extends RuntimeException {
    public BookRepositoryException(String message) {
        super(message);
    }

    public BookRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
