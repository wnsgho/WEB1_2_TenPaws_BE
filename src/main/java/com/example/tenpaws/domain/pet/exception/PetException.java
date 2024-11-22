package com.example.tenpaws.domain.pet.exception;

public class PetException extends RuntimeException {
    public PetException(String message) {
        super(message);
    }

    public PetException(String message, Throwable cause) {
        super(message, cause);
    }
}