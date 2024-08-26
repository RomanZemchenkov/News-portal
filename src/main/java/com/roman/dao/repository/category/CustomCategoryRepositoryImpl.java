package com.roman.dao.repository.category;

import com.roman.dao.entity.Category;
import com.roman.dao.filter.CategoryFilter;
import com.roman.dao.filter.Predicate_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository{

    private final EntityManager entityManager;

    @Override
    public List<Category> findByFilter(CategoryFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        query.where(cb.and(filter(cb,root,filter)));

        return entityManager.createQuery(query).getResultList();

    }

    private Predicate[] filter(CriteriaBuilder cb, Root<Category> root, CategoryFilter filter){
        Predicate_ predicate = Predicate_.of();
        String title = filter.getTitle();
        if(!title.isEmpty()){
            predicate.add(title, t -> cb.equal(root.get("title"),t));
        }
        return predicate.finish().toArray(new Predicate[0]);
    }
}
