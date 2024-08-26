package com.roman.service.dto.category;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShowCategoryWithoutNewsDto {

    private final String id;
    private final String title;
    private final String countOfNews;
    private final String authorId;

    public ShowCategoryWithoutNewsDto(String id, String title, String countOfNews, String authorId) {
        this.id = id;
        this.title = title;
        this.countOfNews = countOfNews;
        this.authorId = authorId;
    }
}
