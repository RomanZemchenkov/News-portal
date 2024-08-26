package com.roman.dao.repository.news;

import com.roman.dao.entity.News;
import com.roman.dao.filter.NewsFilter;
import com.roman.dao.filter.Predicate_;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomNewsRepositoryImpl implements CustomNewsRepository {

    private final EntityManager entityManager;

    @Autowired
    public CustomNewsRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Page<News> findNewsByFilter(NewsFilter filter) {
        return findNewsByFilter(filter, PageRequest.of(0,Integer.MAX_VALUE));
    }


    @Override
    public Page<News> findNewsByFilter(NewsFilter filter, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<News> query = cb.createQuery(News.class);
        Root<News> root = query.from(News.class);

        query.where(cb.and(filter(cb,root,filter)));

        if(pageable.getSort().isSorted() && pageable.isPaged()){
            Sort sort = pageable.getSort();
            List<Order> orders = sort.stream()
                    .map(order -> order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty())))
                    .toList();
            query.orderBy(orders);
        }

        TypedQuery<News> newsQuery = entityManager.createQuery(query);
        EntityGraph<?> graph = entityManager.getEntityGraph("News.withComment");
        newsQuery.setHint("jakarta.persistence.loadgraph",graph);

        if(pageable.isPaged()){
            newsQuery.setFirstResult((int) pageable.getOffset());
            newsQuery.setMaxResults(pageable.getPageSize());
        }

        List<News> news = newsQuery.getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<News> countRoot = countQuery.from(News.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(filter(cb,countRoot,filter));

        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(news,pageable,totalCount);
    }


    private Predicate[] filter(CriteriaBuilder cb, Root<News> root, NewsFilter filter) {
        Predicate_ predicate = Predicate_.of();
        Long customerId = filter.getCustomerId();
        if (customerId != null) {
            predicate.add(customerId, id -> cb.equal(root.get("customer").get("id"), id));
        }
        Long categoryId = filter.getCategoryId();
        if (categoryId != null){
            predicate.add(categoryId, id -> cb.equal(root.get("category").get("id"),id));
        }
        String title = filter.getTitle();
        if(title != null){
            predicate.add(title, t -> cb.equal(root.get("title"),t));
        }
        List<Predicate> predicates = predicate.finish();
        return predicates.toArray(new Predicate[0]);
    }
}
