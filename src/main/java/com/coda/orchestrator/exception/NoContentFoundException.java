package com.coda.orchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentFoundException extends RuntimeException{

    public NoContentFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoContentFoundException() {
    }
}
