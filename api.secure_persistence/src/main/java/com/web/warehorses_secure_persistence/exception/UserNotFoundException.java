package com.web.warehorses_secure_persistence.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Exception at the query: User not found in DB");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
