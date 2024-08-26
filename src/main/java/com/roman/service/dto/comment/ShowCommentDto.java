package com.roman.service.dto.comment;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ShowCommentDto {

    private final String id;
    private final String text;
    private final String author;

    public ShowCommentDto(String id, String text, String author) {
        this.id = id;
        this.text = text;
        this.author = author;
    }
}
