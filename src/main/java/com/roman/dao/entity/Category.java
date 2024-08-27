package com.roman.dao.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "category", schema = "public")
@Getter
@Setter
@ToString(exclude = {"news","customer"})
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "Category.withNews",
        attributeNodes = {@NamedAttributeNode(value = "news")}
)
public class Category implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "title")
    private String title;

    @JsonBackReference(value = "customer-categories")
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @JsonManagedReference(value = "category-news")
    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<News> news = new ArrayList<>();

    public Category(){}

    public Category(String title, Customer customer) {
        this.title = title;
        this.customer = customer;
        customer.getCategories().add(this);
    }

    public Category(Long id, String title, Customer customer) {
        this.id = id;
        this.title = title;
        this.customer = customer;
        customer.getCategories().add(this);
    }
}
