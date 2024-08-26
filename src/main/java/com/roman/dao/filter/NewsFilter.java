package com.roman.dao.filter;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsFilter {

    private Long categoryId;
    private Long customerId;
    private String title;

    private NewsFilter(){}

    public static class Builder{
        private final NewsFilter newsFilter;

        public Builder(){
            this.newsFilter = new NewsFilter();
        }

        public Builder categoryId(Long id){
            newsFilter.setCategoryId(id);
            return this;
        }
        public Builder customerId(Long id){
            newsFilter.setCustomerId(id);
            return this;
        }

        public Builder title(String title){
            newsFilter.setTitle(title);
            return this;
        }

        public NewsFilter build(){
            return newsFilter;
        }
    }
}
