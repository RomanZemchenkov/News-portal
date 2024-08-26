package com.roman.service.dto.customer;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import static com.roman.service.validation.ExceptionMessage.CUSTOMER_PASSWORD_EXCEPTION;
import static com.roman.service.validation.ExceptionMessage.CUSTOMER_USERNAME_EMPTY_EXCEPTION;

@Getter
public class UpdateCustomerDto {

    private final String id;
    @NotEmpty(message = CUSTOMER_USERNAME_EMPTY_EXCEPTION)
    private final String username;
    @Pattern(regexp = "(?=.*[A-Z])(?=.*\\d).{8,}", message = CUSTOMER_PASSWORD_EXCEPTION)
    private final String password;
    @NotEmpty
    private final String firstname;
    @NotEmpty
    private final String lastname;
    private final String birthday;

    public UpdateCustomerDto(String id, String username, String password, String firstname, String lastname, String birthday) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }
}
