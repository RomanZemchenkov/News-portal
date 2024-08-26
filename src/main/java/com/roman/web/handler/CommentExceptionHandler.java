package com.roman.web.handler;

import com.roman.service.validation.exception.CommentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.roman.web.controller")
public class CommentExceptionHandler {

    @ExceptionHandler(value = {CommentException.class})
    public ResponseEntity<ResponseError> commentHandleException(CommentException exception){
        ResponseError error = new ResponseError(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

}
