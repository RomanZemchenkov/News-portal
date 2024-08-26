package com.roman.dao.repository.customer;

import com.roman.dao.entity.Customer;
import com.roman.dao.filter.CustomerFilter;

import java.util.List;

public interface CustomCustomerRepository {

    List<Customer> findByFilter(CustomerFilter filter);
}
