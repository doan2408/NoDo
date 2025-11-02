package com.example.nodotest.Exception.ErrorExcel;

public class ExcelRowLimitExceededException extends RuntimeException {
    public ExcelRowLimitExceededException(String message) {
        super(message);
    }
}