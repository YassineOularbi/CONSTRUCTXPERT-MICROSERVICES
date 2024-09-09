package com.user_service.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super(STR."User not found with id: \{id}");
    }
}
