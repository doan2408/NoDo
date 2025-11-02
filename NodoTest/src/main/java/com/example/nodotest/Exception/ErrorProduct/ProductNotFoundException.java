package com.example.nodotest.Exception.ErrorProduct;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String message){
        super(message);
    }
}
