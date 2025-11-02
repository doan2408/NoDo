package com.example.exception.CustomException;

public abstract class BusinessException extends RuntimeException {
    private final String code; // e.g., "user.not_found"
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    public String getCode() { return code; }
}
