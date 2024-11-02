package com.caloteiros.caloteiro.domain.exceptions;

public class CaloteiroNotFoundException extends RuntimeException {
    public CaloteiroNotFoundException(String message) {
        super(message);
    }
}
