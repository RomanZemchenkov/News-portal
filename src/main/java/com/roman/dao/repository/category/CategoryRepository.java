package com.roman.dao.repository.category;

import com.roman.dao.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface CategoryRepository extends JpaRepository<Category,Long>,CustomCategoryRepository {

    Page<Category> findAllBy(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,attributePaths = {"news"})
    Optional<Category> findCategoryById(Long id);
}
