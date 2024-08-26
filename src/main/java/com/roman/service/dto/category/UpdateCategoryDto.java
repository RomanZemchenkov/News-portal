package com.roman.service.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.roman.service.validation.ExceptionMessage.CATEGORY_TITLE_EXCEPTION;

@Getter
@ToString
@Setter
public class UpdateCategoryDto {

    private final String id;
    @NotBlank(message = CATEGORY_TITLE_EXCEPTION)
    private final String title;

    public UpdateCategoryDto(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
