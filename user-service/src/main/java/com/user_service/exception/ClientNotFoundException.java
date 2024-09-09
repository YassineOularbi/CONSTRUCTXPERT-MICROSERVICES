package com.user_service.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super(STR."Client not found with id: \{id}");
    }
}
