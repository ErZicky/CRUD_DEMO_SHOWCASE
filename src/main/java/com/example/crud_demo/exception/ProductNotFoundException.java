package com.example.crud_demo.exception;


public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(int id) {
        super("Product with id: " + id+ " not found, maybe you have to do a post before");
    }

}
