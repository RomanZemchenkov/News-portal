package com.roman.aop;

import com.roman.dao.entity.Comment;
import com.roman.dao.repository.CommentRepository;
import com.roman.service.dto.comment.UpdateCommentDto;
import com.roman.service.validation.exception.CommentException;
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

import static com.roman.service.validation.ExceptionMessage.*;

@Aspect
@Component
@RequiredArgsConstructor
public class CommentAspect {

    private final CommentRepository repository;

    @Before(value = "com.roman.aop.AnnotationPointcut.hasCheckSessionAnnotation() " +
                    "&& com.roman.aop.AnnotationPointcut.hasCheckAuthorAnnotation()" +
                    "&& com.roman.aop.ClassPointcut.isCommentController() " +
                    "&& args(newsId,dto)",
            argNames = "joinPoint, newsId, dto")
    public void checkAuthorCommentForUpdate(JoinPoint joinPoint, Long newsId, UpdateCommentDto dto) {
        Long id = Long.valueOf(dto.getId());
        checkAuthorComment(id);
    }

    @Before(value = "com.roman.aop.AnnotationPointcut.hasCheckSessionAnnotation() " +
                    "&& com.roman.aop.AnnotationPointcut.hasCheckAuthorAnnotation()" +
                    "&& com.roman.aop.ClassPointcut.isCommentController() " +
                    "&& args(newsId,commentId)",
            argNames = "joinPoint, newsId, commentId")
    public void checkAuthorCommentForUpdate(JoinPoint joinPoint, Long newsId, Long commentId) {
        checkAuthorComment(commentId);
    }


    public void checkAuthorComment(Long commentId) {
        String customerId = getCustomerId();

        Comment comment = repository.findCommentById(commentId).orElseThrow(() -> new CommentException(COMMENT_DOESNT_EXIST_EXCEPTION));

        String authorId = String.valueOf(comment.getCustomer().getId());
        if (!authorId.equals(customerId)) {
            throw new CommentException(COMMENT_AUTHOR_EXCEPTION);
        }
    }

    private String getCustomerId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = request.getSession(false);
        return (String) session.getAttribute("customerId");
    }
}
