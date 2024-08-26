package com.roman.aop;

import com.roman.dao.entity.News;
import com.roman.dao.repository.news.NewsRepository;
import com.roman.service.dto.news.UpdateNewsDto;
import com.roman.service.validation.exception.NewsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.roman.service.validation.ExceptionMessage.NEWS_AUTHOR_EXCEPTION;
import static com.roman.service.validation.ExceptionMessage.NEWS_DOESNT_EXIST_EXCEPTION;

@Aspect
@Component
@RequiredArgsConstructor
public class NewsAspect {

    private final NewsRepository newsRepository;

    @Before(value = "com.roman.aop.ClassPointcut.isControllerLayer() " +
                    "&& com.roman.aop.ClassPointcut.isNewsController()" +
                    "&& com.roman.aop.AnnotationPointcut.hasCheckAuthorAnnotation() " +
                    "&& args(dto)", argNames = "joinPoint,dto")
    public void checkIsRightAuthorForUpdateNews(JoinPoint joinPoint, UpdateNewsDto dto){
        Long newsId = Long.valueOf(dto.getId());
        checkIsRightAuthorNews(newsId);
    }

    @Before(value = "com.roman.aop.ClassPointcut.isControllerLayer() " +
                    "&& com.roman.aop.ClassPointcut.isNewsController()" +
                    "&& com.roman.aop.AnnotationPointcut.hasCheckAuthorAnnotation() " +
                    "&& args(newsId)", argNames = "joinPoint,newsId")
    public void checkIsRightAuthorForUpdateNews(JoinPoint joinPoint, Long newsId){
        checkIsRightAuthorNews(newsId);
    }


    private void checkIsRightAuthorNews(Long newsId){
        String customerId = getCustomerId();

        News news = newsRepository.findById(newsId).orElseThrow(() -> new NewsException(NEWS_DOESNT_EXIST_EXCEPTION.formatted(newsId)));


        String authorId = String.valueOf(news.getCustomer().getId());
        if(!customerId.equals(authorId)){
            throw new NewsException(NEWS_AUTHOR_EXCEPTION);
        }
    }

    private String getCustomerId(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        return (String) session.getAttribute("customerId");
    }
}
