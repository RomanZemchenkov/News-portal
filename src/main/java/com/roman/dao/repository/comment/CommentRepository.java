package com.roman.dao.repository.comment;

import com.roman.dao.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllByNewsId(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"news","customer"})
    Optional<Comment> findCommentById(Long id);
}
