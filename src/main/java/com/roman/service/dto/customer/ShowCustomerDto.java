package com.roman.service.dto.customer;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShowCustomerDto {

    private final String id;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String birthday;

    public ShowCustomerDto(String id, String username, String firstname, String lastname, String birthday) {
        this.id = id;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }
}
