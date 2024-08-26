package com.roman.service.dto.customer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.roman.service.validation.ExceptionMessage.CUSTOMER_PASSWORD_EXCEPTION;
import static com.roman.service.validation.ExceptionMessage.CUSTOMER_USERNAME_EMPTY_EXCEPTION;

@Getter
public class CreateAndLoginCustomerDto {

    @NotEmpty(message = CUSTOMER_USERNAME_EMPTY_EXCEPTION)
    private final String username;
    @Pattern(regexp = "(?=.*[A-Z])(?=.*\\d).{8,}", message = CUSTOMER_PASSWORD_EXCEPTION)
    private final String password;


    public CreateAndLoginCustomerDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
