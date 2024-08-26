package com.roman.dao.repository.news;

import com.roman.dao.entity.News;
import com.roman.dao.filter.NewsFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomNewsRepository {

    Page<News> findNewsByFilter(NewsFilter filter, Pageable pageable);

    Page<News> findNewsByFilter(NewsFilter filter);

}
