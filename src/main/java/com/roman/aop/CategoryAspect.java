package com.roman.aop;

import com.roman.dao.entity.Category;
import com.roman.dao.repository.category.CategoryRepository;
import com.roman.service.dto.category.UpdateCategoryDto;
import com.roman.service.validation.exception.CategoryException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.roman.service.validation.ExceptionMessage.CATEGORY_AUTHOR_EXCEPTION;
import static com.roman.service.validation.ExceptionMessage.CATEGORY_DOESNT_EXIST;

@Aspect
@Component
@RequiredArgsConstructor
public class CategoryAspect {

    private final CategoryRepository categoryRepository;

    @Before(value = "com.roman.aop.ClassPointcut.isControllerLayer() " +
                    "&& com.roman.aop.ClassPointcut.isCategoryController()" +
                    "&& com.roman.aop.AnnotationPointcut.hasCheckAuthorAnnotation() " +
                    "&& args(dto)", argNames = "joinPoint,dto")
    public void checkIsRightAuthorForUpdateCategory(JoinPoint joinPoint, UpdateCategoryDto dto){
        checkIsRightAuthorCategory(Long.valueOf(dto.getId()));
    }

    @Before(value = "com.roman.aop.ClassPointcut.isControllerLayer() " +
                    "&& com.roman.aop.ClassPointcut.isCategoryController()" +
                    "&& com.roman.aop.AnnotationPointcut.hasCheckAuthorAnnotation() " +
                    "&& args(categoryId)", argNames = "joinPoint,categoryId")
    public void checkIsRightAuthorForDeleteCategory(JoinPoint joinPoint, Long categoryId){
        checkIsRightAuthorCategory(categoryId);
    }

    public void checkIsRightAuthorCategory(Long categoryId){
        String customerId = getCustomerId();

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryException(CATEGORY_DOESNT_EXIST.formatted(categoryId)));

        String authorId = Long.toString(category.getCustomer().getId());
        if(!customerId.equals(authorId)){
            throw new CategoryException(CATEGORY_AUTHOR_EXCEPTION);
        }
    }

    private String getCustomerId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        return (String) session.getAttribute("customerId");
    }
}
