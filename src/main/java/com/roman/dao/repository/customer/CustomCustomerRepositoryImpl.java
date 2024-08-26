package com.roman.dao.repository.customer;

import com.roman.dao.entity.Customer;
import com.roman.dao.filter.CustomerFilter;
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
public class CustomCustomerRepositoryImpl implements CustomCustomerRepository{

    private final EntityManager entityManager;

    @Override
    public List<Customer> findByFilter(CustomerFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
        Root<Customer> root = query.from(Customer.class);

        Predicate[] predicates = filter(cb, root, filter);
        query.where(cb.and(predicates));

        return entityManager.createQuery(query).getResultList();
    }

    private Predicate[] filter(CriteriaBuilder cb, Root<Customer> root, CustomerFilter filter){
        Predicate_ predicate = Predicate_.of();
        String username = filter.getUsername();
        if(!username.isEmpty()){
            predicate.add(username, us -> cb.equal(root.get("username"),us));
        }
        List<Predicate> predicates = predicate.finish();
        return predicates.toArray(new Predicate[0]);
    }
}
