package com.roman.web.controller;

import com.roman.BaseTest;
import com.roman.service.dto.category.CreateCategoryDto;
import com.roman.service.dto.category.UpdateCategoryDto;
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
public class CategoryControllerTest extends BaseTest {

    private final MockMvc mock;

    @Autowired
    public CategoryControllerTest(MockMvc mock) {
        this.mock = mock;
    }

    @Test
    @DisplayName("Тест удачного создания категории")
    void successfulCreateCategory() throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "title" : "Music"
                        }
                        """)
                .session(session));

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.title", Matchers.is("Music")));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного создания категории")
    @MethodSource("argumentsForUnsuccessfulCreateCategory")
    void unsuccessfulCreateCategory(CreateCategoryDto dto, boolean hasSession, String expectedExceptionMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }
        ResultActions actions = mock.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "title" : "%s"
                        }
                        """.formatted(dto.getTitle()))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedExceptionMessage)));

    }

    static Stream<Arguments> argumentsForUnsuccessfulCreateCategory() {
        return Stream.of(
                Arguments.of(new CreateCategoryDto("IT"), true, CATEGORY_EXIST_EXCEPTION.formatted("IT")),
                Arguments.of(new CreateCategoryDto("Music"), false, LOGGED_EXCEPTION),
                Arguments.of(new CreateCategoryDto(""), true, CATEGORY_TITLE_EXCEPTION)
        );
    }

    @Test
    @DisplayName("Тест удачного поиска категории по id")
    void successfulFindCategoryById() throws Exception {
        ResultActions actions = mock.perform(get("/api/categories/1"));
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is("1")))
                .andExpect(jsonPath("$.title", Matchers.is("IT")))
                .andExpect(jsonPath("$.newsTitle", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Тест неудачного поиска категории по id")
    void unsuccessfulFindCategoryById() throws Exception {
        ResultActions actions = mock.perform(get("/api/categories/100"));
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is(CATEGORY_DOESNT_EXIST.formatted(100))));
    }

    @ParameterizedTest
    @DisplayName("Тест поиска всех категорий")
    @MethodSource("argumentsForFindAllCategories")
    void findAllCategories(String page, String size, int expectedCountOfCategories, String expectedCountOfNews) throws Exception {
        ResultActions actions = mock.perform(get("/api/categories")
                .param("page", page)
                .param("size", size));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(expectedCountOfCategories)));
        if (expectedCountOfCategories != 0) {
            actions.andExpect(jsonPath("$[0].countOfNews", Matchers.is(expectedCountOfNews)));
        }
    }

    static Stream<Arguments> argumentsForFindAllCategories() {
        return Stream.of(
                Arguments.of("0", "10", 2, "2"),
                Arguments.of("0", "1", 1, "2"),
                Arguments.of("1", "1", 1, "1"),
                Arguments.of("2", "1", 0, "0")
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного обновления категории")
    @MethodSource("argumentsForSuccessfulUpdateCategory")
    void successfulUpdateCategory(UpdateCategoryDto dto, int expectedCountOfNews) throws Exception {
        MockHttpSession session = loginCustomer();
        String title = dto.getTitle();
        ResultActions actions = mock.perform(put("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "id" : "%s",
                           "title" : "%s"
                        }
                        """.formatted(dto.getId(), title))
                .session(session));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", Matchers.is(title)))
                .andExpect(jsonPath("$.authorId", Matchers.is("1")))
                .andExpect(jsonPath("$.newsTitle", Matchers.hasSize(expectedCountOfNews)));
    }

    static Stream<Arguments> argumentsForSuccessfulUpdateCategory() {
        return Stream.of(
                Arguments.of(new UpdateCategoryDto("1", "Music"), 2),
                Arguments.of(new UpdateCategoryDto("2", "Games"), 1)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного обновления категории")
    @MethodSource("argumentsForUnsuccessfulUpdateCategory")
    void unsuccessfulUpdateCategory(UpdateCategoryDto dto, boolean hasSession, String expectedExceptionMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }
        String title = dto.getTitle();
        ResultActions actions = mock.perform(put("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "id" : "%s",
                           "title" : "%s"
                        }
                        """.formatted(dto.getId(), title))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateCategory() {
        return Stream.of(
                Arguments.of(new UpdateCategoryDto("10", "Music"), true, CATEGORY_DOESNT_EXIST.formatted(10)),
                Arguments.of(new UpdateCategoryDto("2", "IT"), true, CATEGORY_EXIST_EXCEPTION.formatted("IT")),
                Arguments.of(new UpdateCategoryDto("1", "Music"), false, LOGGED_EXCEPTION)

        );
    }

    @Test
    @DisplayName("Тест удачного удаления категории")
    void successfulDeleteCategory() throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(delete("/api/categories/1")
                .session(session));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message", Matchers.is("Category with id 1 was deleted.")));

    }

    @ParameterizedTest
    @DisplayName("Тест неудачного удаления категории")
    @MethodSource("argumentsForUnsuccessfulDeleteCategory")
    void unsuccessfulDeleteCategory(String categoryId, boolean hasSession ,String expectedExceptionMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if(!hasSession){
            session.invalidate();
        }
        ResultActions actions = mock.perform(delete("/api/categories/%s".formatted(categoryId))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",Matchers.is(expectedExceptionMessage)));
    }

    static Stream<Arguments> argumentsForUnsuccessfulDeleteCategory(){
        return Stream.of(
                Arguments.of("1",false,LOGGED_EXCEPTION),
                Arguments.of("10",true,CATEGORY_DOESNT_EXIST.formatted(10))
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
