package com.roman;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

@Sql(value = {"classpath:sql/init.sql","classpath:sql/load.sql"})
@ActiveProfiles("test")
public class BaseTest {


    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.3");
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.4.0")
            .withExposedPorts(6379);

    @BeforeAll
    static void init(){
        container.start();
        redisContainer.start();
    }

    @DynamicPropertySource
    static void serProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url",container::getJdbcUrl);
        List<Integer> exposedPorts = redisContainer.getExposedPorts();
        System.out.println(exposedPorts);
        registry.add("spring.redis.port", redisContainer::getExposedPorts);
        registry.add("spring.redis.host", redisContainer::getHost);
    }
}
