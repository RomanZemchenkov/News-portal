package com.roman.service.dto.news;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

import static com.roman.service.validation.ExceptionMessage.NEWS_TEXT_EMPTY_EXCEPTION;
import static com.roman.service.validation.ExceptionMessage.NEWS_TITLE_EMPTY_EXCEPTION;

@Getter
@ToString
public class UpdateNewsDto {

    private final String id;
    @NotEmpty(message = NEWS_TITLE_EMPTY_EXCEPTION)
    private final String title;
    @NotEmpty(message = NEWS_TEXT_EMPTY_EXCEPTION)
    private final String text;
    private final String categoryId;

    public UpdateNewsDto(String id, String title, String text, String categoryId) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.categoryId = categoryId;
    }
}
