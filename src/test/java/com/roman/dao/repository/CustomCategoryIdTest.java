package com.roman.dao.repository;

import com.roman.BaseTest;
import com.roman.dao.entity.Category;
import com.roman.dao.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@Transactional(propagation = Propagation.REQUIRES_NEW)
@SpringBootTest
public class CustomCategoryIdTest extends BaseTest {

    private final CategoryRepository repository;

    @Autowired
    public CustomCategoryIdTest(CategoryRepository repository) {
        this.repository = repository;
    }

    @Test
    @DisplayName("Тест получения категории по id")
    void findCategoryByIdTest(){
        Category category = assertDoesNotThrow(() -> repository.findCategoryById(1L));

        assertThat(category.getTitle()).isEqualTo("IT");
    }
}
