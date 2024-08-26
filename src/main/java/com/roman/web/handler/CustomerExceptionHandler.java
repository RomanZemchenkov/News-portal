package com.roman.web.handler;

import com.roman.service.validation.exception.CustomerException;
import com.roman.service.validation.exception.CustomerNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice(basePackages = "com.roman.web.controller")
public class CustomerExceptionHandler {

    @ExceptionHandler(value = {CustomerException.class})
    public ResponseEntity<ResponseError> customerHandleException(CustomerException exception){
        ResponseError error = new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomerNotExistException.class})
    public ResponseEntity<ResponseError> customerExistHandleException(CustomerNotExistException exception){
        ResponseError error = new ResponseError(exception.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

}
