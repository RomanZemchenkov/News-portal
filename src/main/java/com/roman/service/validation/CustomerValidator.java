package com.roman.service.validation;

import com.roman.dao.entity.Customer;
import com.roman.dao.filter.CustomerFilter;
import com.roman.dao.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerRepository repository;

    public List<Customer> checkUsernameExist(String username){
        CustomerFilter filter = new CustomerFilter.Builder().username(username).build();
        return repository.findByFilter(filter);
    }

    public Optional<Customer> checkCustomerExist(Long id){
        return repository.findById(id);
    }
}
