package com.roman.service.dto.comment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;

import static com.roman.service.validation.ExceptionMessage.COMMENT_EMPTY_EXCEPTION;

@Getter
@ToString
public class CreateCommentDto {

    @NotBlank(message = COMMENT_EMPTY_EXCEPTION)
    private final String text;

    @JsonCreator
    public CreateCommentDto(@JsonProperty("text") String text) {
        this.text = text;
    }
}
