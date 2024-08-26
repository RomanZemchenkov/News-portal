package com.roman.dao.repository;

import com.roman.dao.entity.News;
import com.roman.dao.filter.NewsFilter;
import com.roman.dao.repository.news.NewsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public class NewsRepositoryTest extends BaseRepositoryTest{

    private final NewsRepository newsRepository;
    private static final Long EXIST_NEWS_ID = 1L;

    @Autowired
    public NewsRepositoryTest(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Test
    @DisplayName("Получение новости со всеми комментариями")
    void findNewsWithAllComments(){
        Optional<News> mayBeNews = newsRepository.findNewsWithAllInformationById(EXIST_NEWS_ID);

        assertThat(mayBeNews.isPresent()).isTrue();

        mayBeNews.ifPresent(news -> assertThat(news.getComments()).hasSize(3));
    }

    @ParameterizedTest
    @DisplayName("Тест поиска с использованием пангинации")
    @MethodSource("argumentsForFindAllByPageableAndFilterTest")
    void findAllByPageableAndFilter(NewsFilter filter,PageRequest pageRequest, int expectedPageCount, int expectedNewsCount){
        Page<News> news = newsRepository.findNewsByFilter(filter,pageRequest);

        assertThat(news.getTotalPages()).isEqualTo(expectedPageCount);
        assertThat(news.getTotalElements()).isEqualTo(expectedNewsCount);
    }

    static Stream<Arguments> argumentsForFindAllByPageableAndFilterTest(){
        return Stream.of(
                Arguments.of(new NewsFilter.Builder().customerId(1L).build(),PageRequest.of(0,1),2,2),
                Arguments.of(new NewsFilter.Builder().customerId(1L).categoryId(1L).build(),PageRequest.of(0,2),1,1),
                Arguments.of(new NewsFilter.Builder().customerId(1L).categoryId(2L).build(),PageRequest.of(0,1),1,1)
        );
    }

}
