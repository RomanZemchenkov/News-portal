package com.roman.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnnotationPointcut {

    @Pointcut(value = "@annotation(com.roman.aop.annotation.CheckSession))")
    public void hasCheckSessionAnnotation(){}

    @Pointcut(value = "@annotation(com.roman.aop.annotation.CheckAuthor)")
    public void hasCheckAuthorAnnotation(){}

    @Pointcut(value = "@annotation(com.roman.aop.annotation.CheckExist)")
    public void hasCheckExistAnnotation(){}
}
