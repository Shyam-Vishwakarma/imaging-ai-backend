package com.imaging.app.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super("Access Denied: " + message);
    }
}
