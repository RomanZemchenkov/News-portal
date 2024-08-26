package com.roman.service.dto.category;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ShowCategoryDto {

    private final String id;
    private final String title;
    private final List<String> newsTitle;
    private final String authorId;

    public ShowCategoryDto(String id, String title, List<String> newsTitle, String authorId) {
        this.id = id;
        this.title = title;
        this.newsTitle = newsTitle;
        this.authorId = authorId;
    }
}
