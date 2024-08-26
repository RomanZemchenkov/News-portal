package com.roman.service.mapper;

import com.roman.dao.entity.Comment;
import com.roman.dao.entity.Customer;
import com.roman.dao.entity.News;
import com.roman.service.dto.comment.CreateCommentDto;
import com.roman.service.dto.comment.ShowCommentDto;
import com.roman.service.dto.comment.UpdateCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "dto.text")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "news", source = "news")
    Comment mapToComment(CreateCommentDto dto, News news, Customer customer);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "text")
    Comment mapToComment(UpdateCommentDto dto, @MappingTarget Comment comment);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "text", source = "text")
    @Mapping(target = "author", source = "customer.username")
    ShowCommentDto mapToShow(Comment comment);


}
