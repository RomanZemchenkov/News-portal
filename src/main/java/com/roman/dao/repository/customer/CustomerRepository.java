package com.roman.dao.repository.customer;

import com.roman.dao.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public interface CustomerRepository extends JpaRepository<Customer,Long>, CustomCustomerRepository {

    Page<Customer> findAllBy(Pageable pageable);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,attributePaths = {"customerInfo"})
    Optional<Customer> findCustomerById(Long id);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD,attributePaths = {"customerInfo","comments","news"})
    Long deleteCustomerById(Long id);
}
