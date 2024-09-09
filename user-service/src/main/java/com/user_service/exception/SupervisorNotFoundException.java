package com.user_service.exception;

public class SupervisorNotFoundException extends RuntimeException {
    public SupervisorNotFoundException(Long id) {
        super(STR."Supervisor not found with id: \{id}");
    }
}
