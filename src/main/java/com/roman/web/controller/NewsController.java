package com.roman.web.controller;

import com.roman.annotation.CheckAuthor;
import com.roman.annotation.CheckSession;
import com.roman.service.NewsService;
import com.roman.service.dto.news.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService service;

    @PostMapping
    @CheckSession
    public ResponseEntity<CreatedNewsDto> create(@RequestBody @Validated CreateNewsDto dto,
                                                             HttpSession session){
        String authorId = (String) session.getAttribute("customerId");
        CreatedNewsDto createdNews = service.create(dto, authorId);
        return new ResponseEntity<>(createdNews,HttpStatus.CREATED);
    }

    @PutMapping
    @CheckSession
    @CheckAuthor
    public ResponseEntity<ShowNewsDto> update(@RequestBody @Validated UpdateNewsDto dto){
        ShowNewsDto updatedNews = service.update(dto);
        return new ResponseEntity<>(updatedNews,HttpStatus.OK);
    }

    @GetMapping("/{newsId}")
    public ResponseEntity<ShowNewsDto> findById(@PathVariable("newsId") Long newsId){
        ShowNewsDto news = service.findById(newsId);
        return new ResponseEntity<>(news,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ShowNewsWithoutCommentsDto>> findAll(@RequestParam("size") int size,
                                                              @RequestParam("page") int page){
        List<ShowNewsWithoutCommentsDto> news = service.findAll(page, size);
        return new ResponseEntity<>(news,HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ShowNewsWithoutCommentsDto>> findByFilter(@RequestBody FilterNewsDto dto){
        List<ShowNewsWithoutCommentsDto> news = service.findAllByFilter(dto);
        if(news.isEmpty()){
            return new ResponseEntity<>(news,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(news,HttpStatus.OK);
    }

    @DeleteMapping("/{newsId}")
    @CheckSession
    @CheckAuthor
    public ResponseEntity<Response> delete(@PathVariable("newsId") Long id){
        service.delete(id);
        Response response = new Response("News with id %s was deleted.".formatted(id));
        return new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
    }
}
