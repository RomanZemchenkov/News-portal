package com.roman.service.dto.news;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreatedNewsDto {

    private final String id;
    private final String title;
    private final String text;
    private final String categoryTitle;
    private final String author;
    private final String countOfComments;

    public CreatedNewsDto(String id, String title, String text, String categoryTitle, String author) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.categoryTitle = categoryTitle;
        this.author = author;
        this.countOfComments = "0";
    }
}
