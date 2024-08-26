package com.roman.service;

import com.roman.BaseTest;
import com.roman.service.dto.comment.CreateCommentDto;
import com.roman.service.dto.comment.UpdateCommentDto;
import com.roman.service.dto.news.ShowNewsDto;
import com.roman.service.validation.exception.CommentException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
public class CommentServiceIT extends BaseTest {

    private final CommentService service;
    private static final String EXIST_NEWS_ID = "1";
    private static final String EXIST_CUSTOMER_ID = "1";
    private static final String EXIST_COMMENT_ID = "1";

    @Autowired
    public CommentServiceIT(CommentService service) {
        this.service = service;
    }

    @Test
    @DisplayName("Тест удачного создания комментария")
    void successfulCreateComment(){
        ShowNewsDto news = assertDoesNotThrow(
                () -> service.create(new CreateCommentDto("Something text"),Long.valueOf(EXIST_NEWS_ID), Long.valueOf(EXIST_CUSTOMER_ID)));

        assertThat(news.getComments()).hasSize(4);
    }

    @Disabled
    @Test
    @DisplayName("Тест неудачного создания комментария")
    void unsuccessfulCreateComment(){
        assertThrows(CommentException.class,() -> service.create(new CreateCommentDto(""),Long.valueOf(EXIST_NEWS_ID),Long.valueOf(EXIST_CUSTOMER_ID)));
    }

    @Test
    @DisplayName("Тест удачного обновления комментария")
    void successfulUpdateComment(){
        ShowNewsDto news = assertDoesNotThrow(
                () -> service.update(new UpdateCommentDto(EXIST_COMMENT_ID, "New text")));

        assertThat(news.getComments()).hasSize(3);
        assertThat(news.getComments().get(2).getText()).isEqualTo("New text");
    }

    @Disabled
    @ParameterizedTest
    @DisplayName("Тест неудачного обновления комментария")
    @MethodSource("argumentsForUnsuccessfulUpdateComment")
    void unsuccessfulUpdateComment(UpdateCommentDto dto, Class<? extends Throwable> expectedException){
        assertThrows(expectedException,
                () -> service.update(dto));
    }

    static Stream<Arguments> argumentsForUnsuccessfulUpdateComment(){
        return Stream.of(
                Arguments.of(new UpdateCommentDto(EXIST_COMMENT_ID, ""),CommentException.class),
                Arguments.of(new UpdateCommentDto("100", "Valid text"),CommentException.class)
        );
    }

    @Test
    @DisplayName("Тест удачного удаления комментария")
    void successfulDeleteComment(){
        assertDoesNotThrow(() -> service.delete(1L,1L));
    }
}
