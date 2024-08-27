package com.roman.service;

import com.roman.dao.entity.*;
import com.roman.dao.repository.category.CategoryRepository;
import com.roman.dao.repository.comment.CommentRepository;
import com.roman.dao.repository.customer.CustomerRepository;
import com.roman.dao.repository.news.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Profile("init")
@Service
@RequiredArgsConstructor
public class InitService {

    private final CustomerRepository customerRepository;
    private final CategoryRepository categoryRepository;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;


    public List<Customer> initCustomers(){
        List<Customer> customerList = new ArrayList<>();
        for(int i = 0; i < 50; i++){
            String firstname = "UserFirstname%d".formatted(i);
            String lastname = "UserLastname%d".formatted(i);
            String username = "Username%d".formatted(i);
            String password = "pass%dworD4".formatted(i);

            CustomerInfo customerInfo = new CustomerInfo(firstname, lastname, LocalDate.now());
            customerList.add(new Customer(username, password,customerInfo));
        }
        return customerRepository.saveAll(customerList);
    }

    public List<Category> initCategory(List<Customer> customerList){
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 200; i++){
            int random = new Random().nextInt(50);
            categories.add(new Category("News%d".formatted(i),customerList.get(random)));
        }
        return categoryRepository.saveAll(categories);
    }

    public List<News> initNews(List<Category> categories, List<Customer> customers){
        List<News> newsList = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            int categoryNum = new Random().nextInt(200);
            int customerNum = new Random().nextInt(50);
            newsList.add(new News("Title%d".formatted(i),"Text",categories.get(categoryNum),customers.get(customerNum)));
        }
        return newsRepository.saveAll(newsList);
    }

    public void initComment(List<News> news, List<Customer> customers){
        List<Comment> comments = new ArrayList<>();
        for(int i = 0; i < 20000; i++){
            int newsNum = new Random().nextInt(1000);
            int customerNum = new Random().nextInt(50);
            comments.add(new Comment("Comment text %d".formatted(i),news.get(newsNum),customers.get(customerNum)));
        }
        commentRepository.saveAll(comments);
    }
}
