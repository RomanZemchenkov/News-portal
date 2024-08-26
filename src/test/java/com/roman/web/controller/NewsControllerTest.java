package com.roman.web.controller;

import com.roman.BaseTest;
import com.roman.service.dto.news.CreateNewsDto;
import com.roman.service.dto.news.FilterNewsDto;
import com.roman.service.dto.news.UpdateNewsDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static com.roman.service.validation.ExceptionMessage.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class NewsControllerTest extends BaseTest {

    private final MockMvc mock;
    private static final String EXIST_NEWS_TITLE = "Test IT news";
    private static final String EXIST_NEWS_ID = "1";

    @Autowired
    public NewsControllerTest(MockMvc mock) {
        this.mock = mock;
    }

    @Test
    @DisplayName("Тест удачного создания новости")
    void successfulCreateNews() throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "title" : "Top 10 of profession in future.",
                           "text" : "IT,IT,IT,IT,IT,IT,IT,IT,IT,Factory.",
                           "categoryId" : "1"
                        }
                        """)
                .session(session));

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is("4")))
                .andExpect(jsonPath("$.author", Matchers.is("Kollega1484")))
                .andExpect(jsonPath("$.countOfComments", Matchers.is("0")));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного создания новости")
    @MethodSource("argumentsForUnsuccessfulCreateNews")
    void unsuccessfulCreateNews(CreateNewsDto dto, boolean hasSession, String expectedMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }

        ResultActions actions = mock.perform(post("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "title" : "%s",
                           "text" : "%s",
                           "categoryId" : "%s"
                        }
                        """.formatted(dto.getTitle(), dto.getText(), dto.getCategoryId()))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedMessage)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulCreateNews() {
        return Stream.of(
                Arguments.of(new CreateNewsDto("New technologies.", "Text", "1"), false, LOGGED_EXCEPTION),
                Arguments.of(new CreateNewsDto(EXIST_NEWS_TITLE, "Text", "1"), true, NEWS_TITLE_EXIST_EXCEPTION.formatted(EXIST_NEWS_TITLE)),
                Arguments.of(new CreateNewsDto("New technologies", "Text", "3"), true, CATEGORY_DOESNT_EXIST.formatted(3)),
                Arguments.of(new CreateNewsDto("", "Text", "1"), true, NEWS_TITLE_EMPTY_EXCEPTION),
                Arguments.of(new CreateNewsDto("New technologies", "", "1"), true, NEWS_TEXT_EMPTY_EXCEPTION)
        );
    }

    @Test
    @DisplayName("Тест удачного обновления новости")
    void successfulUpdateNews() throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(put("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "id" : "1",
                           "title" : "Top 10 of profession in future.",
                           "text" : "IT,IT,IT,IT,IT,IT,IT,IT,IT,Factory.",
                           "categoryId" : "1"
                        }
                        """)
                .session(session));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.author", Matchers.is("Kollega1484")))
                .andExpect(jsonPath("$.categoryTitle", Matchers.is("IT")))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(3)));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного обновления новости")
    @MethodSource("argumentsForUnsuccessfulUpdateNews")
    void unsuccessfulUpdateNews(UpdateNewsDto dto, boolean hasSession, String expectedMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }

        ResultActions actions = mock.perform(put("/api/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "id" : "%s",
                           "title" : "%s",
                           "text" : "%s",
                           "categoryId" : "%s"
                        }
                        """.formatted(dto.getId(), dto.getTitle(), dto.getText(), dto.getCategoryId()))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedMessage)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateNews() {
        return Stream.of(
                Arguments.of(new UpdateNewsDto(EXIST_NEWS_ID, "New technologies.", "Text", "1"), false, LOGGED_EXCEPTION),
                Arguments.of(new UpdateNewsDto("100", "New technologies.", "Text", "1"), true, NEWS_DOESNT_EXIST_EXCEPTION.formatted(100)),
                Arguments.of(new UpdateNewsDto(EXIST_NEWS_ID, "", "Text", "1"), true, NEWS_TITLE_EMPTY_EXCEPTION),
                Arguments.of(new UpdateNewsDto(EXIST_NEWS_ID, "New technologies.", "", "1"), true, NEWS_TEXT_EMPTY_EXCEPTION),
                Arguments.of(new UpdateNewsDto(EXIST_NEWS_ID, "New technologies.", "Text", "100"), true, CATEGORY_DOESNT_EXIST.formatted(100)),
                Arguments.of(new UpdateNewsDto(EXIST_NEWS_ID, "Test animal news", "Text", "1"), true, NEWS_TITLE_EXIST_EXCEPTION.formatted("Test animal news"))
        );
    }

    @Test
    @DisplayName("Тест удачного поиска новости по id")
    void successfulFindNewsById() throws Exception {
        ResultActions actions = mock.perform(get("/api/news/1"));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.author", Matchers.is("Kollega1484")))
                .andExpect(jsonPath("$.categoryTitle", Matchers.is("IT")))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Тест неудачного поиска новости по id")
    void unsuccessfulFindNewsById() throws Exception {
        ResultActions actions = mock.perform(get("/api/news/100"));

        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is(NEWS_DOESNT_EXIST_EXCEPTION.formatted(100))));
    }

    @ParameterizedTest
    @DisplayName("Тест поиска все новостей по страницам")
    @MethodSource("argumentsForFindAllNewsByPageable")
    void findAllNewsByPageable(String size, String page, int expectedCountOfNews, String expectedCountOfComments, String author, String title) throws Exception {
        ResultActions actions = mock.perform(get("/api/news")
                .param("size", size)
                .param("page", page));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(expectedCountOfNews)));
        if (expectedCountOfNews > 0) {
            actions.andExpect(jsonPath("$[0].countOfComments", Matchers.is(expectedCountOfComments)))
                    .andExpect(jsonPath("$[0].title", Matchers.is(title)))
                    .andExpect(jsonPath("$[0].author", Matchers.is(author)));
        }

    }

    static Stream<Arguments> argumentsForFindAllNewsByPageable(){
        return Stream.of(
                Arguments.of("10","0",3,"3","Kollega1484","Test IT news"),
                Arguments.of("1","0",1,"3","Kollega1484","Test IT news"),
                Arguments.of("1","1",1,"3","Kollega1484","Test animal news"),
                Arguments.of("2","2",0,"0",null,null)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест поиска новостей по фильтру")
    @MethodSource("argumentsForFindNewsByFilter")
    void findNewsByFilter(FilterNewsDto filter, int expectedCountOfNews) throws Exception {
        ResultActions actions = mock.perform(post("/api/news/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "categoryId" : "%s",
                           "customerId" : "%s",
                           "size" : "%s",
                           "page" : "%s"
                        }
                        """.formatted(filter.getCategoryId(), filter.getCustomerId(), filter.getSize(), filter.getPage())));

        actions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$",Matchers.hasSize(expectedCountOfNews)));
    }

    static Stream<Arguments> argumentsForFindNewsByFilter(){
        return Stream.of(
                Arguments.of(new FilterNewsDto("","","0","0"),3),
                Arguments.of(new FilterNewsDto("","","1","1"),1),
                Arguments.of(new FilterNewsDto("","","2","0"),3),
                Arguments.of(new FilterNewsDto("1","","0","0"),2),
                Arguments.of(new FilterNewsDto("1","","1","1"),1),
                Arguments.of(new FilterNewsDto("","1","0","0"),2),
                Arguments.of(new FilterNewsDto("","1","2","0"),2),
                Arguments.of(new FilterNewsDto("","1","2","1"),0),
                Arguments.of(new FilterNewsDto("1","2","0","0"),1),
                Arguments.of(new FilterNewsDto("","2","0","0"),1)
        );
    }

    @Test
    @DisplayName("Тест удачного удаления новости")
    void successfulDeleteNews() throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(delete("/api/news/1")
                .session(session));

        actions.andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message",Matchers.is("News with id 1 was deleted.")));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного удаления новости")
    @MethodSource("argumentsForUnsuccessfulDeleteNews")
    void unsuccessfulDeleteNews(String newsId, boolean hasSession, String expectedMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if(!hasSession){
            session.invalidate();
        }
        ResultActions actions = mock.perform(delete("/api/news/%s".formatted(newsId))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",Matchers.is(expectedMessage)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulDeleteNews(){
        return Stream.of(
                Arguments.of("1",false, LOGGED_EXCEPTION),
                Arguments.of("100",true, NEWS_DOESNT_EXIST_EXCEPTION.formatted(100)),
                Arguments.of("3",true, NEWS_AUTHOR_EXCEPTION)
        );
    }

    MockHttpSession loginCustomer() throws Exception {
        MvcResult mvcResult = mock.perform(post("/api/customers/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "username" : "Kollega1484",
                           "password" : "passworD4"
                        }
                        """)).andReturn();
        return (MockHttpSession) mvcResult.getRequest().getSession();
    }
}
