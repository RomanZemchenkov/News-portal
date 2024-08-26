package com.roman.dao.repository.news;

import com.roman.dao.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NewsRepository extends JpaRepository<News,Long>, CustomNewsRepository {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, value = "News.withAllAndCommentsWithCustomer")
    Optional<News> findNewsWithAllInformationById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"customer","category"})
    Page<News> findAllBy(Pageable pageable);

    Optional<News> findNewsByTitle(String title);

}
