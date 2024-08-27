package com.roman.dao.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "customer", schema = "public")
@Getter
@Setter
@ToString(exclude = {"customerInfo","comments","news","categories"})
@EqualsAndHashCode(of = "id")
public class Customer implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @JsonManagedReference(value = "customer-categories")
    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();

    @OneToOne(mappedBy = "customer", cascade = {CascadeType.REMOVE,CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private CustomerInfo customerInfo;

    @JsonManagedReference(value = "customer-comments")
    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Comment> comments = new HashSet<>();

    @JsonManagedReference(value = "customer-news")
    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<News> news = new HashSet<>();

    public Customer(){}

    public Customer(String username, String password, CustomerInfo customerInfo) {
        this.username = username;
        this.password = password;
        this.customerInfo = customerInfo;
        customerInfo.setCustomer(this);
    }

    public Customer(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void setCustomerInfo(CustomerInfo customerInfo){
        customerInfo.setCustomer(this);
        this.customerInfo = customerInfo;
    }
}
