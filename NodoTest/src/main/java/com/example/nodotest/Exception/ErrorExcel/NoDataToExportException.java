package com.example.nodotest.Exception.ErrorExcel;

public class NoDataToExportException extends RuntimeException {
    public NoDataToExportException(String message) {
        super(message);
    }
}
