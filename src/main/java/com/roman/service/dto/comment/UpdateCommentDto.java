package com.roman.service.dto.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import static com.roman.service.validation.ExceptionMessage.COMMENT_EMPTY_EXCEPTION;

@ToString
@Getter
public class UpdateCommentDto {

    private final String id;
    @NotBlank(message = COMMENT_EMPTY_EXCEPTION)
    private final String text;

    @JsonCreator
    public UpdateCommentDto(String id, String text) {
        this.id = id;
        this.text = text;
    }
}
