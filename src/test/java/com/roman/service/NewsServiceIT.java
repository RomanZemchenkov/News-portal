package com.roman.service;

import com.roman.BaseTest;
import com.roman.service.dto.news.*;
import com.roman.service.validation.exception.CategoryException;
import com.roman.service.validation.exception.CustomerException;
import com.roman.service.validation.exception.NewsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class NewsServiceIT extends BaseTest {

    private final NewsService service;
    private static final String[] EXIST_CATEGORY_ID = {"1", "2"};
    private static final String[] EXIST_CATEGORY_TITLE = {"IT", "Animal"};
    private static final String[] EXIST_CUSTOMER_ID = {"1", "2", "3"};
    private static final String[] EXIST_AUTHOR = {"Kollega1484", "Izumi", "DungeonMaster"};
    private static final String[] EXIST_NEWS_TITLE = {"Test IT news", "Test animal news"};
    private static final Long[] EXIST_NEWS_ID = {1L,2L};

    @Autowired
    public NewsServiceIT(NewsService service) {
        this.service = service;
    }

    @ParameterizedTest
    @DisplayName("Тест удачного создания новости")
    @MethodSource("argumentsForSuccessfulCreateNews")
    void successfulCreateNews(CreateNewsDto dto, String categoryTitle, String author, String authorId) {
        CreatedNewsDto createdNews = Assertions.assertDoesNotThrow(() -> service.create(dto, authorId));

        assertThat(createdNews.getCategoryTitle()).isEqualTo(categoryTitle);
        assertThat(createdNews.getAuthor()).isEqualTo(author);
    }

    static Stream<Arguments> argumentsForSuccessfulCreateNews() {
        return Stream.of(
                Arguments.of(new CreateNewsDto("News test 1", "Test 1", EXIST_CATEGORY_ID[0]), EXIST_CATEGORY_TITLE[0], EXIST_AUTHOR[0], EXIST_CUSTOMER_ID[0]),
                Arguments.of(new CreateNewsDto("News test 2", "Test 2", EXIST_CATEGORY_ID[1]), EXIST_CATEGORY_TITLE[1], EXIST_AUTHOR[1], EXIST_CUSTOMER_ID[1]),
                Arguments.of(new CreateNewsDto("News test 3", "Test 3", EXIST_CATEGORY_ID[0]), EXIST_CATEGORY_TITLE[0], EXIST_AUTHOR[2], EXIST_CUSTOMER_ID[2]),
                Arguments.of(new CreateNewsDto("News test 4", "Test 4", EXIST_CATEGORY_ID[1]), EXIST_CATEGORY_TITLE[1], EXIST_AUTHOR[0], EXIST_CUSTOMER_ID[0])
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного создания новости")
    @MethodSource("argumentsForUnsuccessfulCreateNews")
    void unsuccessfulCreateNews(CreateNewsDto dto,  String authorId, Class<? extends Throwable> expectedException) {
        Assertions.assertThrows(expectedException, () -> service.create(dto, authorId));
    }

    static Stream<Arguments> argumentsForUnsuccessfulCreateNews() {
        return Stream.of(
                Arguments.of(new CreateNewsDto(EXIST_NEWS_TITLE[0], "Test 1", EXIST_CATEGORY_ID[0]),EXIST_CUSTOMER_ID[0], NewsException.class),
                Arguments.of(new CreateNewsDto(EXIST_NEWS_TITLE[1], "Test 2", EXIST_CATEGORY_ID[1]),EXIST_CUSTOMER_ID[0], NewsException.class),
                Arguments.of(new CreateNewsDto("News test 3", "Test 3", "100"),EXIST_CUSTOMER_ID[0], CategoryException.class),
                Arguments.of(new CreateNewsDto("News test 4", "Test 4", EXIST_CATEGORY_ID[1]),"100", CustomerException.class)
        );
    }

    @Test
    @DisplayName("Тест удачного поиска новости по id")
    void successfulFindNewsById(){
        ShowNewsDto showNewsDto = Assertions.assertDoesNotThrow(() -> service.findById(EXIST_NEWS_ID[0]));

        System.out.println(showNewsDto);

        assertThat(showNewsDto.getAuthor()).isEqualTo(EXIST_AUTHOR[0]);
        assertThat(showNewsDto.getCategoryTitle()).isEqualTo(EXIST_CATEGORY_TITLE[0]);
        assertThat(showNewsDto.getComments()).hasSize(3);
        assertThat(showNewsDto.getComments().get(0).getAuthor()).isEqualTo(EXIST_AUTHOR[0]);
    }

    @ParameterizedTest
    @DisplayName("Тест поиска новостей при помощи pageable")
    @MethodSource("argumentsForFindNewsByPageable")
    void findNewsByPageable(int pageNumber, int pageSize, int expectedResult, String expectedCountOfComments){
        List<ShowNewsWithoutCommentsDto> news = Assertions.assertDoesNotThrow(() -> service.findAll(pageNumber, pageSize));
        System.out.println(news);

        assertThat(news).hasSize(expectedResult);
        if(!news.isEmpty()){
            ShowNewsWithoutCommentsDto showNews = news.get(0);
            assertThat(showNews.getCountOfComments()).isEqualTo(expectedCountOfComments);
        }
    }

    static Stream<Arguments> argumentsForFindNewsByPageable(){
        return Stream.of(
                Arguments.of(0,2,2,"3"),
                Arguments.of(0,1,1,"3"),
                Arguments.of(1,1,1,"3"),
                Arguments.of(2,2,0,"0")
        );
    }

    @ParameterizedTest
    @DisplayName("Тест поиска новостей при помощи фильтра")
    @MethodSource("argumentsForFindNewsByFilterWithPageable")
    void findNewsByFilterWithPageable(FilterNewsDto filterDto, int countOfNews){
        List<ShowNewsWithoutCommentsDto> showNews = Assertions.assertDoesNotThrow(()
                -> service.findAllByFilter(filterDto));

        assertThat(showNews.size()).isEqualTo(countOfNews);
    }

    static Stream<Arguments> argumentsForFindNewsByFilterWithPageable(){
        return Stream.of(
                Arguments.of(new FilterNewsDto("1","","0","0"),2),
                Arguments.of(new FilterNewsDto("","1","0","0"),2),
                Arguments.of(new FilterNewsDto("1","1","0","0"),1),
                Arguments.of(new FilterNewsDto("1","2","0","0"),1),
                Arguments.of(new FilterNewsDto("2","1","0","0"),1),
                Arguments.of(new FilterNewsDto("1","1","0","1"),1),
                Arguments.of(new FilterNewsDto("1","1","1","2"),0)
                );
    }


    @ParameterizedTest
    @DisplayName("Тест удачного обновления новости")
    @MethodSource("argumentsForSuccessfulUpdateNews")
    void successfulUpdateNewsTest(UpdateNewsDto dto){
        Assertions.assertDoesNotThrow(() -> service.update(dto));
    }

    static Stream<Arguments> argumentsForSuccessfulUpdateNews(){
        return Stream.of(
                Arguments.of(new UpdateNewsDto(String.valueOf(EXIST_NEWS_ID[0]), "Random title name", "Random title text", EXIST_CATEGORY_ID[0])),
                Arguments.of(new UpdateNewsDto(String.valueOf(EXIST_NEWS_ID[1]), "Random title name1", "Random title text", EXIST_CATEGORY_ID[1]))
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного обновления новости")
    @MethodSource("argumentsForUnsuccessfulUpdateNews")
    void unsuccessfulUpdateNewsTest(UpdateNewsDto dto, Class<? extends Throwable> expectedException){
        Assertions.assertThrows(expectedException,() -> service.update(dto));
    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateNews(){
        return Stream.of(
                Arguments.of(new UpdateNewsDto(String.valueOf(EXIST_NEWS_ID[0]), "Test animal news", "text", EXIST_CATEGORY_ID[0]),NewsException.class),
                Arguments.of(new UpdateNewsDto(String.valueOf(EXIST_NEWS_ID[0]), "Test animal news", "text", "3"),CategoryException.class)
        );
    }

    @Test
    @DisplayName("Тест удачного удаления новости")
    void successfulDeleteNewsTest(){
        Long id = Assertions.assertDoesNotThrow(() -> service.delete(EXIST_NEWS_ID[1]));
        assertThat(id).isEqualTo(EXIST_NEWS_ID[1]);
    }

    @Test
    @DisplayName("Тест неудачного удаления новости")
    void unsuccessfulDeleteNewsTest(){
        Assertions.assertThrows(NewsException.class,() -> service.delete(4L));
    }

}
