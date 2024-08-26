package com.roman.dao.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerFilter {

    private Long id;
    private String username;

    private CustomerFilter(){}

    public static class Builder{
        private final CustomerFilter filter;

        public Builder(){
            this.filter = new CustomerFilter();
        }

        public Builder username(String username){
            filter.setUsername(username);
            return this;
        }

        public Builder id(Long id){
            filter.setId(id);
            return this;
        }

        public CustomerFilter build(){
            return filter;
        }
    }
}
