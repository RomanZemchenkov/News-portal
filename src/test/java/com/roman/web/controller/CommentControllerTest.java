package com.roman.web.controller;

import com.roman.BaseTest;
import com.roman.service.dto.comment.CreateCommentDto;
import com.roman.service.dto.comment.UpdateCommentDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
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
public class CommentControllerTest extends BaseTest {

    private final MockMvc mock;

    @Autowired
    public CommentControllerTest(MockMvc mock) {
        this.mock = mock;
    }

    @ParameterizedTest
    @DisplayName("Тест удачного создания комментария")
    @MethodSource("argumentsForSuccessfulCreateComment")
    void successfulCreateCommentTest(CreateCommentDto dto, String newsId, int expectedCountOfComments) throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(post("/api/news/%s/comments".formatted(newsId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "text" : "%s"
                        }
                        """.formatted(dto.getText()))
                .session(session));

        actions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(newsId)))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(expectedCountOfComments)));

    }

    static Stream<Arguments> argumentsForSuccessfulCreateComment() {
        return Stream.of(
                Arguments.of(new CreateCommentDto("something comment"), "1", 4),
                Arguments.of(new CreateCommentDto("something comment"), "2", 4),
                Arguments.of(new CreateCommentDto("something comment"), "3", 3)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного создания комментария")
    @MethodSource("argumentsForUnsuccessfulCreateComment")
    void unsuccessfulCreateCommentTest(CreateCommentDto dto, String newsId, boolean hasSession, String expectedMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }

        ResultActions actions = mock.perform(post("/api/news/%s/comments".formatted(newsId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                           "text" : "%s"
                        }
                        """.formatted(dto.getText()))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedMessage)));

    }

    static Stream<Arguments> argumentsForUnsuccessfulCreateComment() {
        return Stream.of(
                Arguments.of(new CreateCommentDto("something comment"), "1", false, LOGGED_EXCEPTION),
                Arguments.of(new CreateCommentDto(""), "2", true, COMMENT_EMPTY_EXCEPTION)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного обновления комментария")
    @MethodSource("argumentsForSuccessfulUpdateComment")
    void successfulUpdateCommentTest(UpdateCommentDto dto, String newsId, int expectedCountOfComments) throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(put("/api/news/%s/comments".formatted(newsId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id" : "%s",
                           "text" : "%s"
                        }
                        """.formatted(dto.getId(), dto.getText()))
                .session(session));

        actions.andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", Matchers.is(newsId)))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(expectedCountOfComments)));

    }

    static Stream<Arguments> argumentsForSuccessfulUpdateComment() {
        return Stream.of(
                Arguments.of(new UpdateCommentDto("2", "something comment"), "1", 3),
                Arguments.of(new UpdateCommentDto("3", "something comment"), "2", 3)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного обновления комментария")
    @MethodSource("argumentsForUnsuccessfulUpdateComment")
    void unsuccessfulUpdateCommentTest(UpdateCommentDto dto, String newsId, boolean hasSession, String expectedMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }
        ResultActions actions = mock.perform(put("/api/news/%s/comments".formatted(newsId))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id" : "%s",
                           "text" : "%s"
                        }
                        """.formatted(dto.getId(), dto.getText()))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedMessage)));

    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateComment() {
        return Stream.of(
                Arguments.of(new UpdateCommentDto("1", "Something text"), "1", false, LOGGED_EXCEPTION),
                Arguments.of(new UpdateCommentDto("1", ""), "1", true, COMMENT_EMPTY_EXCEPTION),
                Arguments.of(new UpdateCommentDto("100", "Something text"), "1", true, COMMENT_DOESNT_EXIST_EXCEPTION),
                Arguments.of(new UpdateCommentDto("4", "Something text"), "2", true, COMMENT_AUTHOR_EXCEPTION)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест удачного удаления комментария")
    @MethodSource("argumentsForSuccessfulDeleteComment")
    void successfulDeleteCommentTest(String newsId, String commentId, int expectedCountOfComments) throws Exception {
        MockHttpSession session = loginCustomer();
        ResultActions actions = mock.perform(delete("/api/news/%s/comments/%s".formatted(newsId, commentId))
                .session(session));

        actions.andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id", Matchers.is(newsId)))
                .andExpect(jsonPath("$.comments", Matchers.hasSize(expectedCountOfComments)));

    }

    static Stream<Arguments> argumentsForSuccessfulDeleteComment() {
        return Stream.of(
                Arguments.of("1", "1", 2),
                Arguments.of("2", "3", 2)
        );
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного удаления комментария")
    @MethodSource("argumentsForUnsuccessfulDeleteComment")
    void unsuccessfulDeleteCommentTest(String newsId, String commentId, boolean hasSession, String expectedMessage) throws Exception {
        MockHttpSession session = loginCustomer();
        if (!hasSession) {
            session.invalidate();
        }
        ResultActions actions = mock.perform(delete("/api/news/%s/comments/%s".formatted(newsId, commentId))
                .session(session));

        actions.andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message", Matchers.is(expectedMessage)));

    }

    static Stream<Arguments> argumentsForUnsuccessfulDeleteComment() {
        return Stream.of(
                Arguments.of("1", "1", false, LOGGED_EXCEPTION),
                Arguments.of("1", "100", true, COMMENT_DOESNT_EXIST_EXCEPTION),
                Arguments.of("1", "4", true, COMMENT_AUTHOR_EXCEPTION)
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
