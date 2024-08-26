package com.roman.service.mapper;

import com.roman.dao.entity.Category;
import com.roman.dao.entity.Customer;
import com.roman.dao.entity.News;
import com.roman.service.dto.news.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface NewsMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "text", source = "dto.text")
    News mapToNews(CreateNewsDto dto, Category category, Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "title", source = "dto.title")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "text", source = "dto.text")
    News mapToNews(UpdateNewsDto dto,@MappingTarget News existNews, Category category);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "categoryTitle", source = "categoryTitle")
    CreatedNewsDto mapToCreatedDto(News news, String author, String categoryTitle);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "categoryTitle", source = "news.category.title")
    @Mapping(target = "author", source = "customer.username")
    @Mapping(target = "comments", source = "comments")
    ShowNewsDto mapToShow(News news);

    @Mapping(target = "author", source = "author")
    @Mapping(target = "categoryTitle", source = "categoryTitle")
    @Mapping(target = "countOfComments", source = "countOfComments")
    ShowNewsWithoutCommentsDto mapToShow(News news, String author, String categoryTitle, String countOfComments);
}
