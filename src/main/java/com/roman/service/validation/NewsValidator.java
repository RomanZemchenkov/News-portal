package com.roman.service.validation;

import com.roman.dao.entity.News;
import com.roman.dao.repository.news.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NewsValidator {

    private final NewsRepository repository;

    public Optional<News> checkTitle(String title){
        return repository.findNewsByTitle(title);
    }


}
