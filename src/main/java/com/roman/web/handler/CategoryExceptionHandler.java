package com.roman.web.handler;

import com.roman.service.validation.exception.CategoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.roman.web.controller")
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<ResponseError> categoryHandleException(CategoryException e){
        ResponseError error = new ResponseError(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
