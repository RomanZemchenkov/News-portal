package com.roman.service.validation;

import com.roman.dao.entity.Category;
import com.roman.dao.filter.CategoryFilter;
import com.roman.dao.repository.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository repository;

    public List<Category> checkTitle(String title){
        CategoryFilter filter = new CategoryFilter.Builder().title(title).build();
        return repository.findByFilter(filter);
    }

    public Optional<Category> checkCategoryExist(Long id){
        return repository.findById(id);
    }
}
