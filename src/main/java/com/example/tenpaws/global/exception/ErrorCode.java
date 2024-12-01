package com.example.tenpaws.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT, "User already exists"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Phone already exists"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "Invalid input"),

    // Faq
    FAQ_NOT_FOUND(HttpStatus.NOT_FOUND, "Faq not found"),
    FAQ_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "Faq not registered"),
    FAQ_NOT_MODIFIED(HttpStatus.BAD_REQUEST, "Faq not modified"),
    FAQ_NOT_DELETED(HttpStatus.BAD_REQUEST, "Faq not deleted"),
    FAQ_NO_CHILD(HttpStatus.BAD_REQUEST, "This Faq has no children"),

    // ChatRoom
    CHATROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "ChatRoom not found"),
    CHATROOM_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "ChatRoom not registered"),
    CHATROOM_NOT_MODIFIED(HttpStatus.BAD_REQUEST, "ChatRoom not modified"),
    CHATROOM_NOT_DELETED(HttpStatus.BAD_REQUEST, "ChatRoom not deleted"),

    // ChatMessage
    CHAT_MESSAGE_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "ChatMessage not registered"),

    // UnReadChatMessages
    UNREAD_CHAT_MESSAGES_NOT_FOUND(HttpStatus.NOT_FOUND, "Unread ChatMessages not found"),
    UNREAD_CHAT_MESSAGES_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "Unread ChatMessages not registered"),
    UNREAD_CHAT_MESSAGES_NOT_MODIFIED(HttpStatus.BAD_REQUEST, "Unread ChatMessages not modified"),

    // Announcement
    ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Announcement not found"),

    // Notification
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Notification not found"),

    // Shelter
    SHELTER_NOT_FOUND(HttpStatus.NOT_FOUND, "Shelter not found with ID"),
    // Pet
    PET_NOT_FOUND(HttpStatus.NOT_FOUND,"Pet not found with ID"),
    NOT_ASSIGNED(HttpStatus.BAD_REQUEST,"The pet is not assigned to this shelter." ),
    RECOMMENDATION_FAILED(HttpStatus.NO_CONTENT, "Recommendation failed");


    private final HttpStatus httpStatus;
    private final String message;
}
