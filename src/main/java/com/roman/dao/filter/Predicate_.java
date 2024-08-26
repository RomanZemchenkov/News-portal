package com.roman.dao.filter;

import jakarta.persistence.criteria.Predicate;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Predicate_ {

    private final List<Predicate> predicateList = new ArrayList<>();

    private Predicate_(){}

    public static Predicate_ of(){
        return new Predicate_();
    }

    public <T> void add(T filter, Function<T, Predicate> function){
        if (filter != null){
            Predicate predicate = function.apply(filter);
            predicateList.add(predicate);
        }
    }

    public List<Predicate> finish(){
        return predicateList;
    }
}
