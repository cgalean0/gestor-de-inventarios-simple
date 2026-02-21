package com.inventario.exceptions;

public class DuplicateSkuException extends RuntimeException{

    public DuplicateSkuException(String message) {
        super(message);
    }
        
}