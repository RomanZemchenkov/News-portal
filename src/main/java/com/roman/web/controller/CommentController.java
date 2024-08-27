package com.roman.web.controller;

import com.roman.aop.annotation.CheckAuthor;
import com.roman.aop.annotation.CheckSession;
import com.roman.service.CommentService;
import com.roman.service.dto.comment.CreateCommentDto;
import com.roman.service.dto.comment.UpdateCommentDto;
import com.roman.service.dto.news.ShowNewsDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/news/{newsId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @PostMapping
    @CheckSession
    public ResponseEntity<ShowNewsDto> create(@PathVariable("newsId") Long newsId,
                                              @RequestBody @Validated CreateCommentDto dto,
                                              HttpSession session){
        String customerId = ((String) session.getAttribute("customerId"));
        ShowNewsDto news = service.create(dto, newsId, Long.parseLong(customerId));
        return new ResponseEntity<>(news, HttpStatus.CREATED);
    }

    @PutMapping
    @CheckSession
    @CheckAuthor
    public ResponseEntity<ShowNewsDto> update(@PathVariable("newsId") Long newsId,
                                              @RequestBody @Validated UpdateCommentDto dto){
        ShowNewsDto news = service.update(dto);
        return new ResponseEntity<>(news,HttpStatus.ACCEPTED);
    }

    @DeleteMapping({"/{commentId}"})
    @CheckSession
    @CheckAuthor
    public ResponseEntity<ShowNewsDto> delete(@PathVariable("newsId") Long newsId,
                                           @PathVariable("commentId") Long commentId){
        ShowNewsDto news = service.delete(commentId,newsId);
        return new ResponseEntity<>(news,HttpStatus.ACCEPTED);
    }
}
