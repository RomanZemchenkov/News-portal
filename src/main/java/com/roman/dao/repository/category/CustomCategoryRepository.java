package com.roman.dao.repository.category;

import com.roman.dao.entity.Category;
import com.roman.dao.filter.CategoryFilter;

import java.util.List;

public interface CustomCategoryRepository{

    List<Category> findByFilter(CategoryFilter filter);
}
