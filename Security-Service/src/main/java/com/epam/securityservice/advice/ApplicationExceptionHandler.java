package com.epam.securityservice.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleInvalidArgument(MethodArgumentNotValidException ex, WebRequest request){

        Map<String,String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        ex.getBindingResult().getAllErrors().forEach(
                fieldError -> errorMap.put(fieldError.getObjectName(), fieldError.getDefaultMessage())
        );
        ExceptionBody exceptionBody = new ExceptionBody(new Date().toString(),request.getDescription(false), HttpStatus.BAD_REQUEST.toString(),errorMap);
        log.error("MethodArgumentNotValidException: {}",exceptionBody);
        return new ResponseEntity<>(exceptionBody,HttpStatus.BAD_REQUEST);
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
