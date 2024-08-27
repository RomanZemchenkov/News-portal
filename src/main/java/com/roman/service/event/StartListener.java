package com.roman.service.event;

import com.roman.dao.entity.Category;
import com.roman.dao.entity.Customer;
import com.roman.dao.entity.News;
import com.roman.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("init")
@RequiredArgsConstructor
@Component
public class StartListener {

    private final InitService service;

    @EventListener(ContextRefreshedEvent.class)
    public void init(ContextRefreshedEvent event){
        List<Customer> customers = service.initCustomers();
        List<Category> categories = service.initCategory(customers);
        List<News> news = service.initNews(categories, customers);
        service.initComment(news,customers);
    }
}
