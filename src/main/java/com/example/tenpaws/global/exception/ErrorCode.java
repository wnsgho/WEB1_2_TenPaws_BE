package com.example.tenpaws.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Phone already exists"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid input"),

    // Faq
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "Faq not found"),
    FAQ_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "Faq not registered"),
    FAQ_NOT_MODIFIED(HttpStatus.BAD_REQUEST, "Faq not modified"),
    FAQ_NOT_DELETE(HttpStatus.BAD_REQUEST, "Faq not deleted"),
    FAQ_NO_CHILD(HttpStatus.BAD_REQUEST, "This Faq has no children"),

    // Announcement
    ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Announcement not found");

    private final HttpStatus httpStatus;
    private final String message;
}
