package com.roman.dao.repository;

import com.roman.BaseTest;
import com.roman.dao.entity.Category;
import com.roman.dao.entity.Customer;
import com.roman.dao.entity.CustomerInfo;
import com.roman.dao.entity.News;
import com.roman.dao.repository.news.RedisNewsLoader;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisNewsLoaderTest extends BaseTest {

    private static List<News> news;
    private static final Long CATEGORY_ID = 1L;
    private final RedisNewsLoader newsLoader;

    @Autowired
    public RedisNewsLoaderTest(RedisNewsLoader newsLoader) {
        this.newsLoader = newsLoader;
        news = new ArrayList<>();
        CustomerInfo customerInfo = new CustomerInfo("Roman", "Zemchenkov", LocalDate.of(2000, 2, 8));
        Customer customer = new Customer("Kollega1484", "1234567A", customerInfo);
        Category category = new Category(CATEGORY_ID, "Animal", customer);
        for(int i = 0; i < 100; i++){
            String title = "Test%d".formatted(i);
            String text = "text";
            News createdNews = new News(title, text, category, customer);
            news.add(createdNews);
        }
    }

    @Test
    @DisplayName("Тест сохранения новостей")
    void saveNewsTest(){
        Assertions.assertDoesNotThrow(() -> newsLoader.saveNewsByCategory(CATEGORY_ID, news));
    }


    @Disabled
    @ParameterizedTest
    @DisplayName("Тест получения новостей из redis")
    @MethodSource("argumentsForGetNewsFromRedis")
    void getNewsFromRedis(long seconds, int countOfNews) throws InterruptedException {
        Assertions.assertDoesNotThrow(() -> newsLoader.saveNewsByCategory(CATEGORY_ID, news));

        Thread.sleep(seconds);

        List<News> news = newsLoader.getNewsFromRedisByCategoryId(CATEGORY_ID);

        assertThat(news).hasSize(countOfNews);
        System.out.println(news);
    }

    static Stream<Arguments> argumentsForGetNewsFromRedis(){
        return Stream.of(
                Arguments.of(1000L,100),// данный тест работает только при одиночном запуске
                Arguments.of(3000L,0) //данный тест работает, если установить значение меньше 3 секунд
        );
    }



}
