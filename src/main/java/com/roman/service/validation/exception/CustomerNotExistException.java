package com.roman.service.validation.exception;

public class CustomerNotExistException extends RuntimeException{

    public CustomerNotExistException(String message){
        super(message);
    }
}
