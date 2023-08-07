package com.endava.workshops.restexample.application.exceptions;

import jakarta.validation.ValidationException;

public class InvalidInputException extends ValidationException {
    public InvalidInputException(String message) {
        super(message);
    }
}
