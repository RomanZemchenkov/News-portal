package com.roman.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ClassPointcut {

    @Pointcut(value = "within(com.roman.web.controller.*)")
    public void isControllerLayer(){}

    @Pointcut(value = "within(com.roman.web.controller.NewsController)")
    public void isNewsController(){}

    @Pointcut(value = "within(com.roman.web.controller.CategoryController)")
    public void isCategoryController(){}

    @Pointcut(value = "within(com.roman.web.controller.CommentController)")
    public void isCommentController(){}
}
