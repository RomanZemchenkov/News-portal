package com.roman.service.dto.news;

import com.roman.service.dto.comment.ShowCommentDto;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ShowNewsDto {

    private final String id;
    private final String title;
    private final String text;
    private final String categoryTitle;
    private final String author;
    private final List<ShowCommentDto> comments;


    public ShowNewsDto(String id, String title, String text, String categoryTitle, String author, List<ShowCommentDto> comments) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.categoryTitle = categoryTitle;
        this.author = author;
        this.comments = comments;
    }
}
