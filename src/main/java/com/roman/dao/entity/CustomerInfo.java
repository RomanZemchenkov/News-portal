package com.roman.dao.entity;

import com.roman.dao.converter.LocalDateConverter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "customer_info", schema = "public")
@Getter
@Setter
@ToString(exclude = "customer")
@EqualsAndHashCode(of = "id")
public class CustomerInfo implements BaseEntity<Long>{

    @Id
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "birthday")
    @Convert(converter = LocalDateConverter.class)
    private LocalDate birthday;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private Customer customer;

    private static long increment = 1L;

    public CustomerInfo(){
        this.firstname = "Firstname" + increment;
        this.lastname = "Lastname" + increment;
        this.birthday = LocalDate.now();
        increment++;
    }

    public CustomerInfo(String firstname, String lastname, LocalDate birthday) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
    }
}
