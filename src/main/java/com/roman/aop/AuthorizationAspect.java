package com.roman.aop;

import com.roman.web.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
@Order(1)
public class AuthorizationAspect {

    @Before(value = "com.roman.aop.ClassPointcut.isControllerLayer() && com.roman.aop.AnnotationPointcut.hasCheckSessionAnnotation()")
    public void checkCustomerInSession(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute("customerId") == null){
            throw new UnauthorizedException();
        }
    }

}
