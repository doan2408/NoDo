package com.example.nodotest.Exception.ErrorProduct;

public class ProductCodeExistsException extends RuntimeException {
    public ProductCodeExistsException(String message) {
        super(message);
    }

    public ProductCodeExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
