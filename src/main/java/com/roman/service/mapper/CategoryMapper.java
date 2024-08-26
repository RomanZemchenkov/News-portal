package com.roman.service.mapper;

import com.roman.dao.entity.Category;
import com.roman.dao.entity.Customer;
import com.roman.service.dto.category.CreateCategoryDto;
import com.roman.service.dto.category.ShowCategoryDto;
import com.roman.service.dto.category.ShowCategoryWithoutNewsDto;
import com.roman.service.dto.category.UpdateCategoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "customer", source = "customer")
    Category mapToCategory(CreateCategoryDto dto, Customer customer);

    @Mapping(target = "title", source = "title")
    Category mapToCategory(UpdateCategoryDto dto,@MappingTarget Category existCategory);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "countOfNews", expression = "java(String.valueOf(category.getNews().size()))")
    @Mapping(target = "authorId", source = "category.customer.id")
    ShowCategoryWithoutNewsDto mapToShowWithoutNews(Category category);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "newsTitle", expression = "java(category.getNews().stream().map(news -> news.getTitle()).toList())")
    @Mapping(target = "authorId", source = "category.customer.id")
    ShowCategoryDto mapToShow(Category category);

}
