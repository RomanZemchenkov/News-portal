package com.roman.dao.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "comment", schema = "public")
@Getter
@Setter
@ToString(exclude = {"news","customer"})
@EqualsAndHashCode(of = "id")
public class Comment implements BaseEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Comment(){}

    public Comment(String text, News news, Customer customer) {
        this.text = text;
        this.news = news;
        this.customer = customer;
        news.getComments().add(this);
        customer.getComments().add(this);
    }
}
