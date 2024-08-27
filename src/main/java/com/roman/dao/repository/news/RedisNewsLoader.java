package com.roman.dao.repository.news;

import com.roman.dao.entity.News;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisNewsLoader {

    /*
    Не забудь создать слушатель события сохранения новости, чтобы обновлять базу и redis
     */

    @Value("${spring.data.redis.news-live-time}")
    private Long liveTime;

    private final RedisTemplate<String,Object> redisTemplate;
    private static final String CATEGORY_NEWS_KEY = "category:%d:news";

    /*
    Можно реализовать сохранение через отсортированные наборы по, например, количеству комментариев
     */

    public void saveNewsByCategory(Long categoryId, List<News> newsList){
        String key = CATEGORY_NEWS_KEY.formatted(categoryId);
        for(News n : newsList){
            redisTemplate.opsForList().leftPush(key,n);
        }
        redisTemplate.expire(key, Duration.ofSeconds(liveTime));
    }
    public List<News> getNewsFromRedisByCategoryId(Long categoryId){
        String key = CATEGORY_NEWS_KEY.formatted(categoryId);

        return redisTemplate.opsForList().range(key, 0, -1)
                .stream()
                .map(ser -> (News) ser)
                .toList();
    }
}
