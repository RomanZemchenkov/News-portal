package com.roman.dao.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryFilter {

    private String title;

    private CategoryFilter(){}

    public static class Builder{

        private final CategoryFilter filter;

        public Builder(){
            this.filter = new CategoryFilter();
        }

        public Builder title(String title){
            filter.setTitle(title);
            return this;
        }

        public CategoryFilter build(){
            return filter;
        }
    }
}
