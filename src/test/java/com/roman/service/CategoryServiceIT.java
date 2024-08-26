package com.roman.service;

import com.roman.BaseTest;
import com.roman.service.dto.category.CreateCategoryDto;
import com.roman.service.dto.category.ShowCategoryDto;
import com.roman.service.dto.category.ShowCategoryWithoutNewsDto;
import com.roman.service.dto.category.UpdateCategoryDto;
import com.roman.service.validation.exception.CategoryException;
import com.roman.service.validation.exception.CustomerNotExistException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CategoryServiceIT extends BaseTest {

    private final CategoryService service;
    private static final String TITLE_EXIST = "IT";
    private static final Long ID_EXIST = 1L;

    @Autowired
    public CategoryServiceIT(CategoryService service) {
        this.service = service;
    }

    @Test
    @DisplayName("Тест удачного создания категории")
    void successfulCreateCategory() {
        Assertions.assertDoesNotThrow(() -> service.create(new CreateCategoryDto("Music"),"1"));
    }

    @ParameterizedTest
    @DisplayName("Тест неудачного создания категории")
    @MethodSource("argumentsForUnsuccessfulCreateCategory")
    void unsuccessfulCreateCategory(CreateCategoryDto dto, String authorId, Class<? extends Throwable> expectedException) {
        Assertions.assertThrows(expectedException,
                () -> service.create(dto,authorId));
    }

    static Stream<Arguments> argumentsForUnsuccessfulCreateCategory(){
        return Stream.of(
                Arguments.of(new CreateCategoryDto(TITLE_EXIST),"1",CategoryException.class),
                Arguments.of(new CreateCategoryDto(TITLE_EXIST),"1",CategoryException.class),
                Arguments.of(new CreateCategoryDto("Valid title"),"100", CustomerNotExistException.class)
        );
    }

    @ParameterizedTest
    @DisplayName("Поиск всех категорий")
    @MethodSource("argumentsForFindAllCategories")
    void findAllCategoriesByPageable(int pageNumber, int pageSize, int expectedCustomerCount) {
        List<ShowCategoryWithoutNewsDto> categories = service.findAll(pageNumber, pageSize);
        System.out.println(categories);
        assertThat(categories).hasSize(expectedCustomerCount);
    }

    static Stream<Arguments> argumentsForFindAllCategories() {
        return Stream.of(
                Arguments.of(0, 1, 1),
                Arguments.of(0, 2, 2),
                Arguments.of(1, 1, 1),
                Arguments.of(2, 1, 0)
        );
    }

    @ParameterizedTest
    @DisplayName("Поиск категории по id")
    @MethodSource("argumentsForFindCategoryById")
    void findCategoryById(Long id, List<String> newsTitle) {
        ShowCategoryDto category = Assertions.assertDoesNotThrow(() -> service.findCategoryById(id));
        System.out.println(category);
        assertThat(category.getNewsTitle()).isEqualTo(newsTitle);
    }

    static Stream<Arguments> argumentsForFindCategoryById() {
        return Stream.of(
                Arguments.of(1L,List.of("Test IT news", "Computers")),
                Arguments.of(2L,List.of("Test animal news"))
        );
    }

    @Test
    @DisplayName("Тест удачного обновления категории")
    void successfulUpdateCategory(){
        ShowCategoryDto category = Assertions.assertDoesNotThrow(() -> service.update(new UpdateCategoryDto("1", "New Title")));

        System.out.println(category);
        assertThat(category.getTitle()).isEqualTo("New Title");
    }

    @Test
    @DisplayName("Тест неудачного обновления категории")
    void unsuccessfulUpdateCategory(){
        Assertions.assertThrows(CategoryException.class,() -> service.update(new UpdateCategoryDto("1", "Animal")));
    }

    @Test
    @DisplayName("Тест удаления категории")
    @Commit
    void deleteCategoryTest(){
        Long id = Assertions.assertDoesNotThrow(() -> service.deleteById(ID_EXIST));
        assertThat(id).isEqualTo(ID_EXIST);
    }


}
