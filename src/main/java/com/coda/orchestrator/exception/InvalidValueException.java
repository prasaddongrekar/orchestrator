package com.coda.orchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidValueException extends RuntimeException{

    public InvalidValueException(String message) {
        super(message);
    }

    public InvalidValueException(String message, Throwable cause) {
        super(message, cause);
    }
}
