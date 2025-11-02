package com.example.nodotest.Exception;

public class CategoryCodeExistsException extends RuntimeException {
    public CategoryCodeExistsException(String message) {
        super(message);
    }

    public CategoryCodeExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
