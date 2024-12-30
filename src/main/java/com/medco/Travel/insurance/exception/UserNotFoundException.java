package com.medco.Travel.insurance.exception;

public class UserNotFoundException extends RuntimeException{

    public Long getUserId() {
        return userId;
    }

    private final Long userId;

    public UserNotFoundException(Long userId) {
        super("User with ID " + userId + " not found");
        this.userId = userId;
    }

    public UserNotFoundException(String message, Long userId) {
        super(message);
        this.userId = userId;
    }

    public UserNotFoundException(String message, Throwable cause, Long userId) {
        super(message, cause);
        this.userId = userId;
    }

    public UserNotFoundException(Throwable cause, Long userId) {
        super(cause);
        this.userId = userId;
    }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Long userId) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.userId = userId;
    }
}
