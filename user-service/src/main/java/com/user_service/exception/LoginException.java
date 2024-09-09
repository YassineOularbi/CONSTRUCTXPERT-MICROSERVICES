package com.user_service.exception;

public class LoginException extends RuntimeException {

    public LoginException(String message) {
        super(message);
    }

    public static LoginException invalidCredentials() {
        return new LoginException("Invalid username or password.");
    }

    public static LoginException userNotFound() {
        return new LoginException("User not found.");
    }

    public static LoginException authenticationFailed() {
        return new LoginException("Authentication failed. Please try again.");
    }
}