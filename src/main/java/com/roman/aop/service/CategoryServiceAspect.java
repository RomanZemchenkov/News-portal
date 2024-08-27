package com.roman.aop.service;

import com.roman.dao.entity.Category;
import com.roman.dao.repository.category.CategoryRepository;
import com.roman.service.dto.category.UpdateCategoryDto;
import com.roman.service.validation.exception.CategoryException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.roman.service.validation.ExceptionMessage.CATEGORY_DOESNT_EXIST;

@Aspect
@Component
@RequiredArgsConstructor
public class CategoryServiceAspect {

    private final CategoryRepository repository;

    @Before(value = "com.roman.aop.ClassPointcut.isCategoryService() && com.roman.aop.AnnotationPointcut.hasCheckExistAnnotation() && args(categoryId)", argNames = "joinPoint,categoryId")
    public void checkCategoryExistByCategoryId(JoinPoint joinPoint, Long categoryId){
        checkCategoryExist(categoryId);
    }

    @Before(value = "com.roman.aop.ClassPointcut.isCategoryService() && com.roman.aop.AnnotationPointcut.hasCheckExistAnnotation() && args(dto)", argNames = "joinPoint,dto")
    public void checkCategoryExistByDto(JoinPoint joinPoint, UpdateCategoryDto dto){
        checkCategoryExist(Long.valueOf(dto.getId()));
    }

    public void checkCategoryExist(Long categoryId){
        Optional<Category> mayBeCategory = repository.findById(categoryId);
        if(mayBeCategory.isEmpty()){
            throw new CategoryException(CATEGORY_DOESNT_EXIST.formatted(categoryId));
        }
    }
}
