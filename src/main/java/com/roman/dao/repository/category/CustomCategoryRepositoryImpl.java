package com.roman.dao.repository.category;

import com.roman.dao.entity.Category;
import com.roman.dao.entity.News;
import com.roman.dao.filter.CategoryFilter;
import com.roman.dao.filter.Predicate_;
import com.roman.dao.repository.news.RedisNewsLoader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    private final EntityManager entityManager;
    private final RedisNewsLoader newsLoader;

    /*
    Непонимаю, почему при выполнении данного метода новости тянут за собой ещё и комментарии к ним.
    UPD. Разобрался. При преобразовании POJO в Json mapper пытается вытянуть всю инфу и тянет за собой и комментарии
    С этим можно справиться, поставив @JsonIgnore аннотацию, но она вызовет другие проблемы
     */
    @Override
    public Category findCategoryById(Long id) {
        Category category = entityManager.find(Category.class,id);

        List<News> news = newsLoader.getNewsFromRedisByCategoryId(id);
        if(news.isEmpty()){
            news = entityManager.createQuery("SELECT n FROM News AS n  WHERE n.category.id = :id", News.class)
                    .setParameter("id", id)
                    .getResultList();
            newsLoader.saveNewsByCategory(id,news);
        }
        List<News> mergeNews = new ArrayList<>();
        /*
        Я совсем не понимаю данную проблему.
        Если я не буду создавать новый список, а просто будут merge сущностей делать, а потом объединять их с новостями -
        я буду получать ошибку detached entity passed to persist
        Но если создать целиком новый список - всё будет работать.
         */
        for (News n : news) {
            News merge = entityManager.merge(n);
            merge.setCategory(category);
            mergeNews.add(merge);
        }

        category.setNews(mergeNews);

        return category;
    }

    @Override
    public List<Category> findByFilter(CategoryFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = cb.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        query.where(cb.and(filter(cb, root, filter)));

        return entityManager.createQuery(query).getResultList();

    }

    private Predicate[] filter(CriteriaBuilder cb, Root<Category> root, CategoryFilter filter) {
        Predicate_ predicate = Predicate_.of();
        String title = filter.getTitle();
        if (!title.isEmpty()) {
            predicate.add(title, t -> cb.equal(root.get("title"), t));
        }
        return predicate.finish().toArray(new Predicate[0]);
    }
}
