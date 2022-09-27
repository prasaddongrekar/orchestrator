package com.coda.orchestrator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author prasad
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class NoDownStreamFoundException extends RuntimeException{
    public NoDownStreamFoundException(String message) {
        super(message);
    }

    public NoDownStreamFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
