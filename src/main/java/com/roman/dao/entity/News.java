package com.roman.dao.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "news", schema = "public")
@Getter
@Setter
@ToString(exclude = {"category","customer","comments"})
@EqualsAndHashCode(of = "id")
@NamedEntityGraph(
        name = "News.withComment",
        attributeNodes = {@NamedAttributeNode(value = "comments")}
)
@NamedEntityGraph(
        name = "News.withAllAndCommentsWithCustomer",
        attributeNodes =
                {
                        @NamedAttributeNode(value = "category"),
                        @NamedAttributeNode(value = "customer"),
                        @NamedAttributeNode(value = "comments", subgraph = "commentsWithAuthor")
                },
        subgraphs = {
                @NamedSubgraph(
                        name = "commentsWithAuthor",
                        attributeNodes = @NamedAttributeNode(value = "customer")
                )
        }
)
public class News implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @JsonBackReference(value = "category-news")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @JsonManagedReference(value = "news-comments")
    @JsonIgnore
    @OneToMany(mappedBy = "news",fetch = FetchType.LAZY, cascade = {CascadeType.DETACH,CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    @JsonBackReference(value = "customer-news")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public News(){}

    public News(String title, String text, Category category, Customer customer) {
        this.title = title;
        this.text = text;
        this.category = category;
        this.customer = customer;
        category.getNews().add(this);
        customer.getNews().add(this);
    }
}
