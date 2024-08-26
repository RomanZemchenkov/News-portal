package com.roman.service.dto.category;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import static com.roman.service.validation.ExceptionMessage.CATEGORY_TITLE_EXCEPTION;

@Getter
@Setter
public class CreateCategoryDto {

    @NotEmpty(message = CATEGORY_TITLE_EXCEPTION)
    private final String title;

    @JsonCreator
    public CreateCategoryDto(@JsonProperty("title") String title) {
        this.title = title;
    }
}
