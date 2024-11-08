package com.caloteiros.user.domain.exceptions;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }
}