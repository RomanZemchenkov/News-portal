package com.roman.web.handler;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ResponseError {

    private final String message;
    private final int statusCode;

    public ResponseError(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
