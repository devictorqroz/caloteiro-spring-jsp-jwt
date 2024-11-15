package com.caloteiros.user.domain.exceptions;

public class PasswordResetTokenException extends RuntimeException {
    public PasswordResetTokenException(String message) {
        super(message);
    }
}