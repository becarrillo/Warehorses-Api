package com.web.warehorses_secure_persistence.exception;

public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException() {
        super("One Activity that was queried to perform an action was not found");
    }

    public ActivityNotFoundException(String message) {
        super(message);
    }
}
