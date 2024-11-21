package com.example.tenpaws.domain.faq.exception;

public class FaqTaskException extends RuntimeException {
    private final int statusCode;

    public FaqTaskException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
