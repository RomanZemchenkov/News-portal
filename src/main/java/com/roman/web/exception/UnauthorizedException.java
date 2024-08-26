package com.roman.web.exception;


import static com.roman.service.validation.ExceptionMessage.LOGGED_EXCEPTION;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(){
        super(LOGGED_EXCEPTION);
    }
}
