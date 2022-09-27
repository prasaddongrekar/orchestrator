package com.coda.orchestrator.exception;

import com.coda.orchestrator.pojo.WebResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * @author prasad
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidValueException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity<WebResponse> handleInvalidException(Exception e, WebRequest webRequest){

        logger.error("Exception occured ", e);
        WebResponse webResponse = WebResponse.builder().date(new Date())
                .details(webRequest.getDescription(false))
                .errorCode(e.getMessage())
                .message(e.getCause().getLocalizedMessage())
                .build();

        return new ResponseEntity<>(webResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({ NoContentFoundException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public final ResponseEntity<WebResponse> handleNoContentFoundException(Exception e, WebRequest webRequest){

        logger.error("Exception occured ", e);
        WebResponse webResponse = WebResponse.builder().date(new Date())
                .details(webRequest.getDescription(false))
                .errorCode(e.getMessage())
                .message(e.getCause().getLocalizedMessage())
                .build();

        return new ResponseEntity<>(webResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler({NoDownStreamFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<WebResponse> handleNoDownStreamFoundException(Exception e, WebRequest webRequest){

        logger.error("Exception occured ", e);
        WebResponse webResponse = WebResponse.builder().date(new Date())
                .details(webRequest.getDescription(false))
                .errorCode(e.getMessage())
                .message(e.getCause().getLocalizedMessage())
                .build();

        return new ResponseEntity<>(webResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
