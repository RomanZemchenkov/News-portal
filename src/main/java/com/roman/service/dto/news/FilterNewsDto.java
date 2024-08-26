package com.roman.service.dto.news;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode
public class FilterNewsDto {

    private final String categoryId;
    private final String customerId;
    private final String size;
    private final String page;

    public FilterNewsDto(String categoryId, String customerId, String size, String page) {
        this.categoryId = categoryId;
        this.customerId = customerId;
        this.size = size;
        this.page = page;
    }
}
