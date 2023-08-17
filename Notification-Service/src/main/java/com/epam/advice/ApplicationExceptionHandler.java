package com.epam.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {
    private static final String MESSAGE = "message";

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ExceptionBody> handleMailException(MailException ex,WebRequest request)
    {
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put(MESSAGE,ex.getMessage());
        ExceptionBody exceptionBody = new ExceptionBody(new Date().toString(),request.getDescription(false), HttpStatus.BAD_REQUEST.toString(),errorMap);
        log.error("MailException: {}",exceptionBody);
        return new ResponseEntity<>(exceptionBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionBody> handleRuntimeException(RuntimeException ex,WebRequest request)
    {
        Map<String,String> errorMap = new HashMap<>();
        errorMap.put(MESSAGE,ex.getMessage());
        ExceptionBody exceptionBody = new ExceptionBody(new Date().toString(),request.getDescription(false), HttpStatus.BAD_REQUEST.toString(),errorMap);
        log.error("RuntimeException: {}",exceptionBody);
        return new ResponseEntity<>(exceptionBody,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
