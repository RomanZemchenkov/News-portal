package com.roman.service;

import com.roman.dao.entity.Comment;
import com.roman.dao.entity.Customer;
import com.roman.dao.entity.News;
import com.roman.dao.repository.CommentRepository;
import com.roman.dao.repository.customer.CustomerRepository;
import com.roman.dao.repository.news.NewsRepository;
import com.roman.service.dto.comment.CreateCommentDto;
import com.roman.service.dto.comment.UpdateCommentDto;
import com.roman.service.dto.news.ShowNewsDto;
import com.roman.service.mapper.CommentMapper;
import com.roman.service.mapper.NewsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository repository;
    private final CustomerRepository customerRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper mapper;
    private final NewsMapper newsMapper;

    @Transactional
    public ShowNewsDto create(CreateCommentDto dto, Long newsId, Long authorId){

        Customer customer = customerRepository.findById(authorId).get();
        News news = newsRepository.findById(newsId).get();

        Comment comment = mapper.mapToComment(dto, news, customer);
        repository.saveAndFlush(comment);

        return newsMapper.mapToShow(news);
    }

    @Transactional
    public ShowNewsDto update(UpdateCommentDto dto){

        Comment comment = repository.findCommentById(Long.valueOf(dto.getId())).get();

        Comment beforeUpdate = mapper.mapToComment(dto, comment);

        Comment updatedComment = repository.saveAndFlush(beforeUpdate);

        return newsMapper.mapToShow(updatedComment.getNews());
    }

    @Transactional
    public ShowNewsDto delete(Long commentId, Long newsId){
        repository.deleteById(commentId);

        News news = newsRepository.findNewsWithAllInformationById(newsId).get();
        return newsMapper.mapToShow(news);
    }
}
