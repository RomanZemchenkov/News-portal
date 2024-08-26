package com.roman.dao.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category", schema = "public")
@Getter
@Setter
@ToString(exclude = {"news","customer"})
@EqualsAndHashCode(of = "id")
public class Category implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<News> news = new HashSet<>();

    public Category(){}

    public Category(Long id, String title, Customer customer) {
        this.id = id;
        this.title = title;
        this.customer = customer;
        customer.getCategories().add(this);
    }
}
